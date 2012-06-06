function showRegisterPanel(element) {
    ajaxCall(jsRoutes.controllers.Application.registerPanel, function(data) {
        updateElement(element, data);
    });
}