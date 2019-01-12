<%@attribute name="pageName" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security"
           uri="http://www.springframework.org/security/tags" %>

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

<c:choose>
    <c:when test="${pageName == 'Home'}">
        <li class="active"><a href="" data-toggle="collapse" data-target=".nav-collapse"><spring:message
                code="topbar.home"/></a></li>
    </c:when>
    <c:otherwise>
        <li><a href="" data-toggle="collapse" data-target=".nav-collapse"><spring:message code="topbar.home"/></a></li>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${pageName == 'About'}">
        <li class="active" data-toggle="collapse" data-target=".nav-collapse"><a href=""><spring:message
                code="topbar.about"/></a></li>
    </c:when>
    <c:otherwise>
        <li><a href="about" data-toggle="collapse" data-target=".nav-collapse"><spring:message code="topbar.about"/></a>
        </li>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${pageName == 'Contact'}">
        <li class="active" data-toggle="collapse" data-target=".nav-collapse"><a href=""><spring:message
                code="topbar.contact"/></a></li>
    </c:when>
    <c:otherwise>
        <li><a href="contact" data-toggle="collapse" data-target=".nav-collapse"><spring:message
                code="topbar.contact"/></a></li>
    </c:otherwise>
</c:choose>
