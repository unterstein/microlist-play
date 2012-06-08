function showRegisterPanel(element) {
    if(!$(element).find("form").length) {
        ajaxCall(jsRoutes.controllers.Authentication.registerPanel(), function(data) {
            updateElement(element, data);
        });
    } else {
        updateElement(element, "");
    }
}