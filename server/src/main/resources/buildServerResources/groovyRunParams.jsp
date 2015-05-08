<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<link rel="stylesheet" href="${teamcityPluginResourcesPath}codemirror.css">

<forms:workingDirectory/>

<tr id="script.content.container">
  <th>
    <label for="scriptBody">Groovy script: <l:star/></label>
  </th>
  <td>
    <span class="smallNote">A Groovy script which will be executed on the build agent.</span>
      <div class="postRel">
        <textarea id="scriptBody" name="prop:scriptBody">${propertiesBean.properties['scriptBody']}</textarea>
      </div>
    <span class="error" id="error_script.content"></span>
  </td>
</tr>
<script>
    $j.getScript("${teamcityPluginResourcesPath}codemirror.js")
    .done(function () {
        return $j.getScript("${teamcityPluginResourcesPath}groovy.js");
    })
    .done(function () {
        var textarea = $("scriptBody");
        var myCodeMirror = CodeMirror.fromTextArea(textarea, {
            lineNumbers: true,
            matchBrackets: true,
            mode: "groovy"
        });
        myCodeMirror.on("change", function (cm) {
            textarea.value = cm.getValue();
        });
    });
</script>
