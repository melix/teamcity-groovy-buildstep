<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<forms:workingDirectory />

<tr id="script.content.container">
  <th>
    <label for="scriptBody">Groovy script: <l:star/></label>
  </th>
  <td>
    <props:multilineProperty name="scriptBody" className="longField" cols="58" rows="10" expanded="true" linkTitle="Enter Groovy script content"/>
    <span class="error" id="error_script.content"></span>
    <span class="smallNote">A Groovy script which will be executed on the build agent.</span>
  </td>
</tr>