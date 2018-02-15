<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@page import="org.slf4j.LoggerFactory" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page import="org.springframework.http.HttpStatus" %>
<%@ page import="org.springframework.security.oauth2.common.exceptions.OAuth2Exception" %>
<%
    if (request.getAttribute("error") != null && request.getAttribute("error") instanceof OAuth2Exception) {
        request.setAttribute("message", ((OAuth2Exception) request.getAttribute("error")).getOAuth2ErrorCode());
    } else if (request.getAttribute("javax.servlet.error.status_code") != null) {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
            LoggerFactory.getLogger("error.jsp").error("servlet error", t);
        }
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        HttpStatus status = HttpStatus.valueOf(code);
        request.setAttribute("message", status.toString() + " " + status.getReasonPhrase());
    } else {
        request.setAttribute("message", "Error");
    }
%>
<spring:message code="error.title" var="title"/>
<o:header title="${title}"/>
<o:topbar pageName="Error"/>
<div class="container-fluid main">
    <div class="row-fluid">
        <div class="offset1 span10">
            <div class="hero-unit">
                <h1>
                    <span class="text-error"><c:out value="${ message }"/></span>
                </h1>
            </div>
        </div>
    </div>
</div>
<o:footer/>
