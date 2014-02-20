<%--
  ~ Copyright (c) 2006, JetBrains, s.r.o. All Rights Reserved.
  --%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<props:viewWorkingDirectory />

<div class="parameter">
  Groovy script: <props:displayValue name="scriptBody" emptyValue="<empty>" showInPopup="true" popupTitle="Script body" popupLinkText="view script content"/>
</div>
