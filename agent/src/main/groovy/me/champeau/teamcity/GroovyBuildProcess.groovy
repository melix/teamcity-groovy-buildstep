/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.champeau.teamcity

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.AgentRunningBuild
import jetbrains.buildServer.agent.BuildFinishedStatus
import jetbrains.buildServer.agent.BuildProcess
import jetbrains.buildServer.agent.BuildRunnerContext

import java.util.concurrent.*

@CompileStatic
@Log4j
class GroovyBuildProcess implements BuildProcess, Callable<BuildFinishedStatus> {

    private Future<BuildFinishedStatus> buildStatus;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final AgentRunningBuild agent;
    private final BuildRunnerContext context;

    GroovyBuildProcess(final AgentRunningBuild agent, final BuildRunnerContext context) {
        this.agent = agent
        this.context = context
    }

    @Override
    public void start() throws RunBuildException {
        try {
            buildStatus = executor.submit(this);
            log.info("Groovy script started")
        } catch (final RejectedExecutionException e) {
            log.error("Groovy script failed to start", e);
            throw new RunBuildException(e)
        }
    }

    public boolean isInterrupted() {
        return buildStatus.cancelled && finished
    }

    public boolean isFinished() {
        return buildStatus.done
    }

    public void interrupt() {
        log.info("Interrupting Groovy script");
        buildStatus.cancel(true);
    }

    @Override
    BuildFinishedStatus waitFor() throws RunBuildException {
        try {
            final BuildFinishedStatus status = buildStatus.get();
            log.info("Build process was finished");
            status
        } catch (final InterruptedException e) {
            throw new RunBuildException(e);
        } catch (final ExecutionException e) {
            throw new RunBuildException(e);
        } catch (final CancellationException e) {
            log.error("Build process was interrupted: ", e);
            return BuildFinishedStatus.INTERRUPTED;
        } finally {
            executor.shutdown()
        }
    }

    public BuildFinishedStatus call() {
        def binding = new Binding()
        binding.setProperty("system", context.buildParameters.systemProperties)
        binding.setProperty("env", context.buildParameters.environmentVariables)
        binding.setProperty("params", context.buildParameters.allParameters)
        binding.setProperty("agent", agent)
        binding.setProperty("configParams", context.configParameters)
        binding.setProperty("log", agent.buildLogger)
        binding.setProperty("context", context)

        def shell = new GroovyShell(binding)
        def script = shell.parse(context.runnerParameters.scriptBody)
        try {
            def result = script.run()
            agent.buildLogger.message("Script finished with result: $result")
        } catch (e) {
            agent.buildLogger.error(e.toString())
            return BuildFinishedStatus.FINISHED_FAILED
        }

        BuildFinishedStatus.FINISHED_SUCCESS
    }


}
