<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
                    information in our membership administration. Include your NetID and student number (if applicable).
                </c:when>
                <c:when test="${ param.chMemberError == 'invalid'}">
                    We could not find a valid CH membership record linked to your login information. If you are a
                    current CH member, please <a href="https://ch.tudelft.nl/contact/">contact the board</a> so that we
                    can correct your information in our membership administration. Include your NetID and student number
                    (if applicable).
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
