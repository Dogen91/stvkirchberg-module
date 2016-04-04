
[#include "/templating-kit/components/macros/tocMarkup.ftl"/]
[@tocMarkup model content /]

[#assign showInfoText = content.showInfoText!false]
[#assign usernameLabel = content.usernameLabel!i18n['pur.paragraph.stkPURLoginForm.label.username']!]
[#assign passwordLabel = content.passwordLabel!i18n['pur.paragraph.stkPURLoginForm.label.password']!]
[#assign submitLoginLabel = content.submitLoginLabel!i18n['pur.paragraph.stkPURLoginForm.submit.login.label']!]
[#assign submitLogoutLabel = content.submitLogoutLabel!i18n['pur.paragraph.stkPURLoginForm.submit.logout.label']!]
[#assign requiredLabel = content.requiredLabel!i18n['pur.paragraph.stkPURLoginForm.label.required']!]
[#assign requiredCharacter = content.requiredCharacter!i18n['pur.paragraph.stkPURLoginForm.required.character']!]

[#if model.loginError?exists]
    <div class="text error">
        <h2>Error</h2>
        <ul>
            <li>${i18n.getWithDefault(model.loginError, i18n.get('login.defaultError'))}</li>
        </ul>
    </div>
[/#if]

[#if !model.authenticated || cmsfn.editMode]
    [#if cmsfn.editMode && showInfoText]
        <p>${i18n['pur.paragraph.stkPURLoginForm.author.edit.general.infomessage']}</p>
        <p>${i18n['pur.paragraph.stkPURLoginForm.author.edit.login.infomessage']}</p>
    [/#if]

    [#if content.subtitle?has_content || content.text?has_content]
        [#if content.subtitle?has_content]
            <h2 ${id}>${content.subtitle}</h2>
        [/#if]
        [#if content.text?has_content]
            ${cmsfn.decode(content).text}
        [/#if]
    [/#if]
    <div class="form-wrapper">
        <form id="loginForm" method="post" action="" enctype="application/x-www-form-urlencoded">
            [#if content.realmName?has_content]
              <input type="hidden" name="mgnlRealm" value="${content.realmName}"/>
            [/#if]

            <input type="hidden" name="mgnlModelExecutionUUID" value="${content.@uuid}"/>

            <p class="required"><span>${requiredCharacter}</span> ${requiredLabel}</p>

            <fieldset>
                <div>
                    <label for="username"><span>${usernameLabel} <dfn title="required">${requiredCharacter}</dfn></span></label>
                    <input required="required"  type="text" id="username" name="mgnlUserId" value="" maxlength="50"/>
                </div>
                <div>
                    <label class="" for="mgnlUserPSWD"><span>${passwordLabel} <dfn title="required">${requiredCharacter}</dfn></span></label>
                    <input required="required"  type="password" name="mgnlUserPSWD" id="mgnlUserPSWD" value=""/>
                </div>

                <div class="button-wrapper" >
                  <fieldset class="buttons" >
                    <input type="submit" class="submit" accesskey="s" value="${submitLoginLabel}" />
                  </fieldset>
                </div>

            </fieldset>

        </form>
    </div><!-- end form-wrapper -->
[/#if]

[#if model.authenticated]
    [#if cmsfn.editMode && showInfoText]
        <p>${i18n['pur.paragraph.stkPURLoginForm.author.edit.logout.infomessage']}</p>
    [/#if]

    <div class="form-wrapper">
        <form method="post" action="" enctype="application/x-www-form-urlencoded" >
            <div class="form-item-hidden">
                <input type="hidden" name="mgnlModelExecutionUUID" value="${content.@uuid}"/>
                <input type="hidden" name="mgnlLogout" value="true" />
            </div>

            <p>${i18n.get('pur.loggedin',[ctx.user.name])}</p>
            <fieldset>
                <div class="button-wrapper" >
                    <fieldset class="buttons" >
                        <input type="submit" class="submit" accesskey="s" value="${submitLogoutLabel}" />
                    </fieldset>
                </div>
            </fieldset>
        </form>
    </div>
[/#if]