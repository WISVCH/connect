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
<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <base href="${config.issuer}">
    <meta charset="utf-8">
    <title>CH Connect - Log In</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="resources/js/lib/html5.js"></script>
    <![endif]-->

    <!-- favico -->
    <link rel="shortcut icon" href="resources/images/mitreid-connect.ico">

    <!-- Load jQuery up here so that we can use in-page functions -->
    <script type="text/javascript" src="resources/js/lib/jquery.js"></script>
    <script type="text/javascript" charset="UTF-8" src="resources/js/lib/moment-with-locales.js"></script>
    <script type="text/javascript" src="resources/js/lib/i18next.js"></script>
    <script type="text/javascript">
        $.i18n.init({
            fallbackLng: "en",
            lng: "en",
            resGetPath: "resources/js/locale/__lng__/__ns__.json",
            ns: {
                namespaces: ["wisvch","messages"],
                defaultNs: 'wisvch'
            },
            fallbackNS: ["wisvch","messages"]
        });
        moment.locale("en");
        // safely set the title of the application
        function setPageTitle(title) {
            document.title = "CH Connect - " + title;
        }

        // get the info of the current user, if available (null otherwise)
        function getUserInfo() {
            return ;
        }

        // get the authorities of the current user, if available (null otherwise)
        function getUserAuthorities() {
            return ["ROLE_ANONYMOUS"];
        }

        // is the current user an admin?
        // NOTE: this is just for
        function isAdmin() {
            var auth = getUserAuthorities();
            if (auth && _.contains(auth, "ROLE_ADMIN")) {
                return true;
            } else {
                return false;
            }
        }

        var heartMode = false;

    </script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script type="text/javascript">
        <!--

        var checked = localStorage.getItem('alwaysUseNetid');
        <c:if test="${ param.error == null && param.logout == null }">
        if (checked) {
            window.location.replace("${pageContext.request.contextPath}/saml/login");
        }
        </c:if>

        $(document).ready(function () {
            // select the appropriate field based on context
            $('#<c:out value="${ login_hint != null ? 'password' : 'username' }" />').focus();

            var checkbox = $('#alwaysUseNetid');
            checkbox.attr('checked', checked);
            checkbox.change(function () {
                if (this.checked) {
                    localStorage.setItem('alwaysUseNetid', true);
                } else {
                    localStorage.removeItem('alwaysUseNetid');
                }
            });
        });
        //-->
    </script>

    <style>
        :root {
            --ch-black: rgb(0, 0, 0);
            --ch-white: rgb(255, 255, 255);
            --ch-indigo: rgb(0, 39, 74);
            --ch-indigo-transparent: rgba(0, 39, 74, 0.9);
            --ch-blue: rgb(144, 182, 203);
            --ch-blue-faded: rgba(144, 182, 203, 0.5);
            --ch-magenta: rgb(232, 48, 138);
            --ch-orange: rgb(238, 114, 3);
            --ch-orange-hover: rgb(238, 114, 3, 0.85);
            --ch-yellow: rgb(251, 187, 33);
            --ch-turqoise: rgb(42, 161, 169);

            --animation-speed: 0.3s;
        }
        html, body {
            min-height: 100vh;
        }
        body  {
            background-image: url("resources/images/owl.webp");
            -webkit-backdrop-filter: blur(5px);
            backdrop-filter: blur(5px);
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            background-color: var(--ch-blue-faded);
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .login-box {
            padding-left: 30px;
            padding-right: 30px;
            padding-top: 20px;
            padding-bottom: 10px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: flex-start;
            background-color: var(--ch-indigo-transparent);
            color: var(--ch-white);
            border-radius: 0.5em;
        }
        .other{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: flex-start;
            width: 100%;
            margin-bottom: 10px;
        }
        .login-with-btn {
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;
            gap: 10px;
            width: 100%;
            transition: background-color var(--animation-speed), box-shadow var(--animation-speed), color var(--animation-speed);
            padding: 12px;
            margin-top: 15px;
            border: none;
            border-radius: 0.25rem;
            color: #757575;
            font-size: 14px;
            font-weight: 500;
        }
        .login-with-btn:hover {
            box-shadow: 0 -1px 0 rgba(0, 0, 0, .04), 0 2px 4px rgba(0, 0, 0, .25);
            background-color: var(--ch-orange);
            color: white;
            cursor: pointer;
        }
        .login-with-btn img{
            height: calc(14px + 1em);
            transition: filter var(--animation-speed);
        }
        .login-with-btn:hover img{
            filter: brightness(0) saturate(100%) invert(100%) sepia(100%) saturate(2%) hue-rotate(151deg) brightness(106%) contrast(101%);
        }
        button, a[role=button] {
            background-color: var(--ch-white);
            text-decoration: none;
        }
        .chaccount{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: flex-start;
            width: 100%;
        }
        .btn-orange{
            background-color: var(--ch-blue);
            color: var(--ch-white);
            margin-top: 30px;
        }
        .btn-orange:hover{
            background-color: var(--ch-orange-hover);
            color: var(--ch-white);
        }
        input:focus{
            border-color: red;
        }
        @media only screen and (max-width: 576px) {
            .login-box {
                width: 83vw;
            }
        }
        /* // Small devices (landscape phones, 576px and up) */
        @media only screen and (min-width: 576px) and (max-width: 768px) {
            .login-box {
                width: 400px;
            }
        }

        /* // Medium devices (tablets, 768px and up) */
        @media only screen and (min-width: 768px) and (max-width: 992px) {
            .login-box {
                width: 400px;
            }
        }

        /* // Large devices (desktops, 992px and up) */
        @media only screen and (min-width: 992px) and (max-width: 1200px) {
            .login-box {
                width: 400px;
            }
            .logo{
                width: 20vw;
            }
        }

        /* // Extra large devices (large desktops, 1200px and up) */
        @media only screen and (min-width: 1200px) {
            .login-box {
                width: 400px;
            }
        }
    </style>
</head>

<body>
<div class="login-box">
    <!-- <h2>Welcome</h2> -->
    <img style="width: 100%;;" src="resources/images/ChrHuygens_logo-horizontaal_RGB_transparant-wit.png" alt="CH Logo">
    <hr class="w-100" style="border-color: white;" />
    <h4>Log in using</h4>
    <div class="other">
        <a role="button" href="${pageContext.request.contextPath}/saml2/login" class="login-with-btn">
            <img src="resources/images/Google__G__Logo.svg" alt="Google Logo">
            CH Google account
        </a>
        <a role="button" href="${pageContext.request.contextPath}/saml/login" class="login-with-btn">
            <img src="resources/images/TUDelft_logo_cmyk.svg" alt="TU Delft Logo">
            NetID
        </a>
    </div>
    <div class="form-check">
        <input type="checkbox" id="alwaysUseNetid" value="true" class="form-check-input">
        <label class="form-check-label" for="alwaysUseNetid">
            Always log in with NetID on this device
        </label>
    </div>
    <c:if test="${ param.error != null }">
        <div class="alert alert-danger" role="alert" style="width: 100%;">
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
                <c:when test="${ param.chMemberError == 'pre-student'}">
                    Your CH membership has been properly registered, however your student account is marked as "Pre-Student"
                    indicating you account has not been fully enabled yet. Please wait until the academic year has started, 
                    and then you should be able to login. If you are still not able to log-in while you believe you should, 
                    please do not hesitate to <a href="https://ch.tudelft.nl/contact/">contact the board</a> so we can 
                    correct your information in our membership administration. In your message, include your NetID and student 
                    number (if applicable).<br>
                    Welcome to the TU Delft and hope to see you soon!
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
</div>
</body>
</html>
