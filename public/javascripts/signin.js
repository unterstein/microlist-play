function showRegisterPanel(element) {
    if(!$(element).find("form").length) {
        ajaxCall(jsRoutes.controllers.Application.registerPanel(), function(data) {
            updateElement(element, data);
        });
    } else {
        updateElement(element, "");
    }
}