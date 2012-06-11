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
        $('.side.nav .icon-edit:last').click();
    });
}

function customAfterAjaxHandler() {
    $('.side.nav .icon-edit').unbind('click');
    $('.side.nav .icon-edit').click(function(event) {
        toggleText($(this), event);
        return false;
    });
    $('.side.nav input').unbind('keyup');
    $('.side.nav input').bind('keyup', function(e) {
        var key = e.keyCode || e.which;
        if (key === 13) {
            $(document).click();
        }
    });
    $('.side.nav span').unbind('click');
    $('.side.nav span').click(function(event) {
        toggleText($(this).siblings('.icon-edit'), event);
        return false;
    });
    $('.side.nav .icon-trash').unbind('click');
    $('.side.nav .icon-trash').click(function(event) {
        var element = $(this).parents('li');
        ajaxCall(jsRoutes.controllers.TodoList.removeProject($(element).attr('id').replace('project_', '')), function(data) {
            replaceElement($(element), '');
        });
        return false;
    });
    $('.taskList input.newTask').unbind('keyup');
    $('.taskList input.newTask').bind('keyup', function(e) {
        var key = e.keyCode || e.which;
        if (key === 13) {
            var projectId = $(this).attr('projectid');
            var element = $(this).parents('.taskList');
            ajaxCall(jsRoutes.controllers.TodoList.addTask(projectId, $(this).val()), function(data) {
                updateElement($(element), data);
                customAfterAjaxHandler();
            });
        }
    });
    $('.task .taskbox').change(function(event) {
        ajaxCall(jsRoutes.controllers.TodoList.changeTaskState($(this).data("task"), $(this).is(':checked')));
    });
}

$(function() {
    customAfterAjaxHandler();
});