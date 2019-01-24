<%@ page import="fr.mbds.tp.Role" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-user" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.user}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.user}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.user}" method="POST">
                <fieldset class="form">
                    %{--<f:all bean="user"/>--}%

                    <div class="fieldcontain required">
                        <label>Username <span class="required-indicator">*</span> </label>
                        <input type="text" name="username" value="" required="" id="username">
                    </div>

                    <div class="fieldcontain required">
                        <label>Password <span class="required-indicator">*</span> </label>
                        <g:passwordField name="myPasswordField" value="${myPassword}" />
                    </div>

                    <div class="fieldcontain required">
                        <label>First Name <span class="required-indicator">*</span> </label>
                        <input type="text" name="messageContent" value="" required="" id="firstname">
                    </div>

                    <div class="fieldcontain required">
                        <label>Last Name <span class="required-indicator">*</span> </label>
                        <input type="text" name="messageContent" value="" required="" id="lastname">
                    </div>

                    <div class="fieldcontain ">
                        <label>Mail </label>
                        <input type="text" name="messageContent" value=" " required="" id="mail">
                    </div>

                    <div class="fieldcontain">
                        <label>Telephone </label>
                        <input type="text" name="messageContent" value=" " required="" id="telephone">
                    </div>

                    <div class="fieldcontain required">
                        <label>Date of Birth <span class="required-indicator">*</span> </label>
                        <g:datePicker name="myDate" value="${new Date()}"
                                      noSelection="['':'-Choose-']"
                                      precision="day"
                                      relativeYears="[-100..-10]"/>
                    </div>

                    <div class="fieldcontain required">
                        <label>Groupes </label>
                        %{--<input type="text" name="groupes" value="${groupeList.authority}" required="" id="groupes">--}%
                        %{--<g:select id="role" name="role.id" from="${fr.mbds.tp.Role.listOrderByAuthority()}" optionKey="id"--}%
                                  %{--onchange="categoryChanged(this.value);"--}%
                                  %{--value=""--}%
                                  %{--multiple="yes"--}%
                        %{--/>--}%
                        <select class="js-example-basic-multiple" name="states[]" multiple="multiple" style="width: 60%">
                            <g:each in="${fr.mbds.tp.Role.list()}">
                                <option>${it.authority}</option>
                            </g:each>
                        </select>
                    </div>



                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
