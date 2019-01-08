<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@page import="org.slf4j.LoggerFactory" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page import="org.springframework.http.HttpStatus" %>
<%@ page import="org.springframework.security.oauth2.common.exceptions.OAuth2Exception" %>
<%--
  ~ Copyright 2019 W.I.S.V. 'Christiaan Huygens'
  ~ Copyright 2018 The MITRE Corporation
  ~    and the MIT Internet Trust Consortium
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%
    if (request.getAttribute("error") != null && request.getAttribute("error") instanceof OAuth2Exception) {
        OAuth2Exception e = (OAuth2Exception) request.getAttribute("error");
        request.setAttribute("errorCode", "OAuth2 error: " + e.getOAuth2ErrorCode());
        request.setAttribute("message", e.getMessage());
    } else if (request.getAttribute("javax.servlet.error.status_code") != null) {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
            LoggerFactory.getLogger("error.jsp").error("servlet error", t);
        }
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        HttpStatus status = HttpStatus.valueOf(code);
        request.setAttribute("errorCode", status.toString() + " " + status.getReasonPhrase());
    } else {
        request.setAttribute("errorCode", "Error");
    }
%>
<spring:message code="error.title" var="title"/>
<o:header title="${title}"/>
<o:topbar pageName="Error"/>
<div class="container-fluid main">
    <div class="row-fluid">
        <div class="offset1 span10">
            <div class="hero-unit">
                <h1><span class="text-error"><c:out value="${ errorCode }"/></span></h1>
                <p><c:out value="${ message }"/></p>
            </div>
        </div>
    </div>
</div>
<o:footer/>
