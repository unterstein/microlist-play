function toggleText(element, event) {
    $(element).siblings("span").toggle();
    $(element).siblings("input").toggle();
    if($(element).siblings("input").is(":visible")) {
        $(document).click(function(event) { 
            var selector = $(element).siblings("input");
            if($(selector)[0] != $(event.target)[0] && $(event.target).parents().index($(selector)) == -1) {
                if($(selector).is(":visible")) {
                    toggleText(element);
                }
            }
        })
    } else {
        var id = $(element).parents('li').attr('id').replace('project_', '');
        var name = $(element).siblings("input").val();
        updateProject($(element).parents('li'), id, name);
        $(document).unbind('click');
    }
    doAfterAjaxHandling();
    if(event != undefined) {
        event.preventDefault();
    }
}

function updateProject(element, id, name) {
    ajaxCall(jsRoutes.controllers.TodoList.updateProject(id, name), function(data) {
        replaceElement(element, data);
        customAfterAjaxHandler();
    });
}

function addProject(element) {
    ajaxCall(jsRoutes.controllers.TodoList.addProject(), function(data) {
        updateElement(element, data);
        customAfterAjaxHandler();
    });
}

function customAfterAjaxHandler() {
    $('.side.nav .icon-edit').click(function(event) {
        toggleText($(this), event);
        return false;
    });
    $('.side.nav span').click(function(event) {
        toggleText($(this).siblings('.icon-edit'), event);
        return false;
    });
}
$(function() {
    customAfterAjaxHandler();
});