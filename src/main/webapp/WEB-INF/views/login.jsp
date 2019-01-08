<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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

<o:header title="Log In"/>
<script type="text/javascript">
    <!--

    $(document).ready(function () {
        // select the appropriate field based on context
        $('#<c:out value="${ login_hint != null ? 'password' : 'username' }" />').focus();
    });

    //-->
</script>
<o:topbar/>
<div class="container-fluid main">

    <c:if test="${ param.error != null }">
        <div class="alert alert-error">
            <c:choose>
                <c:when test="${ param.chMemberError == 'conflict'}">
                    Your CH membership record contains conflicting information. Please
                    <a href="https://ch.tudelft.nl/contact/">contact the board</a> so that we can correct your
                    information in our membership administration. In your message, include your NetID and student number
                    (if applicable).
                </c:when>
                <c:when test="${ param.chMemberError == 'invalid'}">
                    We could not find a valid CH membership record linked to your login information. If you are a
                    current CH member, please <a href="https://ch.tudelft.nl/contact/">contact the board</a> so that we
                    can correct your information in our membership administration. In your message, include your NetID
                    and student number (if applicable).
                </c:when>
                <c:otherwise>
                    <spring:message code="login.error"/>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <c:if test="${ param.logout != null }">
        <div class="alert alert-success">
            You have been logged out.
        </div>
    </c:if>

    <div class="row-fluid">
        <div class="span6 well" style="height: 15em">
            <h2>Log in with CH Account</h2>

            <form action="${pageContext.request.contextPath}/ldap/login" method="POST">
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-user"></i></span>
                        <input type="text" placeholder="Username" autocorrect="off" autocapitalize="off"
                               spellcheck="false" id="username" name="username"
                               value="<c:out value="${ login_hint }" />">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-lock"></i></span>
                        <input type="password" placeholder="Password" autocorrect="off" autocapitalize="off"
                               spellcheck="false" id="password" name="password">
                    </div>
                </div>
                <div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" class="btn" value="Login" name="submit">
                </div>
            </form>
        </div>

        <div class="span6 well" style="height: 15em">
            <h2>Log in with TU Delft NetID</h2>

            <div><a href="${pageContext.request.contextPath}/saml/login" class="btn btn-info">
                Login with TU Delft NetID
            </a></div>
        </div>
    </div>
</div>

<o:footer/>
