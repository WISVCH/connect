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

    <h1>Login</h1>

    <c:if test="${ param.error != null }">
        <div class="alert alert-error"><spring:message code="login.error"/></div>
    </c:if>


    <div class="row-fluid">
        <div class="span6 well">
            <h2>Login with CH Account</h2>

            <form action="${pageContext.request.contextPath}/ldap/login" method="POST">
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-user"></i></span>
                        <input type="text" placeholder="Username" autocorrect="off" autocapitalize="off"
                               autocomplete="off" spellcheck="false" value="<c:out value="${ login_hint }" />"
                               id="username" name="username">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-lock"></i></span>
                        <input type="password" placeholder="Password" autocorrect="off" autocapitalize="off"
                               autocomplete="off" spellcheck="false" id="password" name="password">
                    </div>
                </div>
                <div>
                    <input type="submit" class="btn" value="Login" name="submit">
                </div>
            </form>
        </div>

        <div class="span6 well">
            <h2>Log in with TU Delft NetID</h2>

            <div><a href="${pageContext.request.contextPath}/saml/login" class="btn btn-info">Login with TU Delft
                NetID</a></div>
        </div>
    </div>
</div>

<o:footer/>
