function toggleProject(element, event) {
    $(element).siblings("span").toggle();
    $(element).siblings("input").toggle();
    if($(element).siblings("input").is(":visible")) {
        $(document).click(function(event) { 
            var selector = $(element).siblings("input");
            if($(selector)[0] != $(event.target)[0] && $(event.target).parents().index($(selector)) == -1) {
                if($(selector).is(":visible")) {
                    toggleProject(element);
                }
            }
        })
    } else {
        var id = $(element).parents('li').attr('id').replace('project_', '');
        var name = $(element).siblings("input").val();
        var selectedProjectId = $(element).parents('ul').children('li.active').attr('id');
        if(selectedProjectId != undefined) {
            var selectedProject = selectedProjectId.replace('project_', '');
            updateProject($(element).parents('li'), id, name, selectedProject);
        }
        $(document).unbind('click');
    }
    doAfterAjaxHandling();
    if(event != undefined) {
        event.preventDefault();
    }
}

function toggleTask(element, event) {
    $(element).siblings("span").toggle();
    $(element).siblings("input").toggle();
    if($(element).siblings("input").is(":visible")) {
        $(document).click(function(event) { 
            var selector = $(element).siblings("input");
            if($(selector)[0] != $(event.target)[0] && $(event.target).parents().index($(selector)) == -1) {
                if($(selector).is(":visible")) {
                    toggleTask(element);
                }
            }
        })
    } else {
        var selectedTask = $(element).parents('.task').attr('id').replace('taskid_', '');
        var name = $(element).siblings("input").val();
        if(selectedTask != undefined) {
            updateTask($(element).parents('.task'), selectedTask, name);
        }
        $(document).unbind('click');
    }
    doAfterAjaxHandling();
    if(event != undefined) {
        event.preventDefault();
    }
}

function updateTask(element, id, name) {
    ajaxCall(jsRoutes.controllers.Tasks.updateTask(id, name), function(data) {
        replaceElement(element, data);
        customAfterAjaxHandler();
    });
}

function updateProject(element, id, name, selectedProject) {
    ajaxCall(jsRoutes.controllers.TodoList.updateProject(id, name, selectedProject), function(data) {
        replaceElement(element, data);
        customAfterAjaxHandler();
    });
}

function addProject(element) {
    ajaxCall(jsRoutes.controllers.TodoList.addProject($(element).find('li.active').attr('id').replace('project_', '')), function(data) {
        updateElement(element, data);
        customAfterAjaxHandler();
        $('.side.nav li:last .icon-edit').click();
    });
}

function customAfterAjaxHandler() {
    $('.side.nav .icon-edit').unbind('click');
    $('.side.nav .icon-edit').click(function(event) {
        toggleProject($(this), event);
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
        toggleProject($(this).siblings('.icon-edit'), event);
        return false;
    });
    $('.side.nav .icon-trash').unbind('click');
    $('.side.nav .icon-trash').click(function(event) {
        var element = $(this).parents('li');
        var selectedProject = $(element).attr('id').replace('project_', '');
        var deletedMyself = $(element).hasClass('active');
        ajaxCall(jsRoutes.controllers.TodoList.removeProject(selectedProject), function(data) {
            if(deletedMyself) {
                window.location.href = $('.side.nav li:first a').attr('href');
            }
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
            ajaxCall(jsRoutes.controllers.Tasks.addTask(projectId, $(this).val()), function(data) {
                updateElement($(element), data);
                customAfterAjaxHandler();
            });
        }
    });
    $('.task .taskbox').unbind('change');
    $('.task .taskbox').change(function(event) {
        ajaxCall(jsRoutes.controllers.Tasks.changeTaskState($(this).data("task"), $(this).is(':checked')));
    });
    $('.task .icon-trash').unbind('click');
    $('.task .icon-trash').click(function(event) {
        var element = $(this).parents('.task');
        var selectedTask = $(element).attr('id').replace('taskid_', '');
        ajaxCall(jsRoutes.controllers.Tasks.removeTask(selectedTask), function(data) {
            replaceElement($(element), '');
        });
        return false;
    });
    $('.task .icon-edit').unbind('click');
    $('.task .icon-edit').click(function(event) {
        toggleTask($(this), event);
        return false;
    });
    $('.task input').unbind('keyup');
    $('.task input').bind('keyup', function(e) {
        var key = e.keyCode || e.which;
        if (key === 13) {
            $(document).click();
        }
    });
}

$(function() {
    customAfterAjaxHandler();
});