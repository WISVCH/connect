<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

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

<spring:message code="home.title" var="title"/>
<o:header title="${title}"/>
<o:topbar pageName="Home"/>
<div class="container-fluid main">
    <div class="row-fluid">
        <o:sidebar/>
        <div class="span10">
            <div class="hero-unit">
                <div class="row-fluid">
                    <div class="span2 visible-desktop"><img src="resources/images/openid_connect_large.png"/></div>

                    <div class="span10">
                        <h1><spring:message code="home.welcome.title"/></h1>
                        <p><spring:message code="home.welcome.body"/></p>
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <h2><spring:message code="home.about.title"/></h2>

                    <p><spring:message code="home.about.body"/></p>

                    <p><a class="btn" href="https://github.com/mitreid-connect/"><spring:message code="home.more"/>
                        &raquo;</a></p>
                </div>
                <div class="span6">
                    <h2><spring:message code="home.contact.title"/></h2>
                    <p>
                        <spring:message code="home.contact.body"/>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<o:footer/>
