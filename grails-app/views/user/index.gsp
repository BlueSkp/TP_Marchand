<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="list-user" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            %{--<f:table collection="${userList}" />--}%
            <table>
                <tr>
                    <g:sortableColumn property="username" title="Username" />
                    <g:sortableColumn property="firstName" title="Firstname" />
                    <g:sortableColumn property="lastName" title="Lastname" />
                    <g:sortableColumn property="mail" title="Mail" />
                    <g:sortableColumn property="tel" title="Telephone" />
                    <g:sortableColumn property="dob" title="Dob" />
                    <g:sortableColumn property="dateCreated" title="DateCreated" />
                    <g:sortableColumn property="lastUpdated" title="LastUpdated" />

                </tr>

                <g:each in="${userList}" var="user">

                    <tr>
                        <td><g:link controller="user" action="show" id="${user.id}">${user.username}</g:link></td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.mail}</td>
                        <td>${user.tel}</td>
                        <td>${user.dob}</td>
                        <td>${user.dateCreated}</td>
                        <td>${user.lastUpdated}</td>
                    </tr>
                </g:each>
            </table>

            <div class="pagination">
                <g:paginate total="${userCount ?: 0}" />
            </div>
        </div>
    </body>
</html>