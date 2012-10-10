/**
 *  Copyright (C) 2012 Johannes Unterstein
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author      Johannes Unterstein <unterstein@me.com>
 * @copyright   Copyright 2012, Johannes Unterstein
 * @license     http://www.gnu.org/licenses/gpl.html GPLv3
 * @project     microlist-play
 */

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
    $(element).siblings("span.taskTitle").toggle();
    $(element).siblings("input[name='title']").toggle();
    if($(element).siblings("input[name='title']").is(":visible")) {
        $(document).click(function(event) { 
            var selector = $(element).siblings("input[name='title']");
            if($(selector)[0] != $(event.target)[0] && $(event.target).parents().index($(selector)) == -1) {
                if($(selector).is(":visible")) {
                    toggleTask(element);
                }
            }
        })
    } else {
        var selectedTask = $(element).parents('.task').attr('id').replace('taskid_', '');
        var name = $(element).siblings("input[name='title']").val();
        if(selectedTask != undefined) {
            updateTaskName($(element).parents('.task'), selectedTask, name);
        }
        $(document).unbind('click');
    }
    doAfterAjaxHandling();
    if(event != undefined) {
        event.preventDefault();
    }
}

function updateTaskName(element, id, name) {
    ajaxCall(jsRoutes.controllers.Tasks.updateTaskName(id, name), function(data) {
        replaceElement(element, data);
        customAfterAjaxHandler();
    });
}

function updateTaskDescription(element, id, description) {
    ajaxCall(jsRoutes.controllers.Tasks.updateTaskDescription(id, description), function(data) {
        var dialog = $(element).find('.modal.fade');
        $(dialog).on('hidden', function() {
            replaceElement(element, data);
            customAfterAjaxHandler();
            $(dialog).unbind('hidden');
        });
        $(dialog).modal('hide');
    });
}

function updateTaskDueDate(element, id, date) {
    ajaxCall(jsRoutes.controllers.Tasks.updateTaskDueDate(id, date), function(data) {
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
    // come on, lets order the entries fancy ;-)
    $('.taskList .task[overdue="true"]').each(function() {
        var element = $(this);
        $(this).remove();
        $('.taskList #overdue').append($(element));
    });
    $('.taskList .task[today="true"]').each(function() {
        var element = $(this);
        $(this).remove();
        $('.taskList #today').append($(element));
    });
    $('.taskList #overdue .task[overdue="false"], .taskList #today .task[today="false"], .taskList #finished .task[finished="false"]').each(function() {
     // please reorganize
        var element = $(this);
        $(this).remove();
        $('.taskList #normal').append($(element));
    });
    $('.taskList .task[finished="true"]').each(function() {
        var element = $(this);
        $(this).remove();
        $('.taskList #finished').append($(element));
    });
    // handle click events
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
            var element = $(this).parents('.taskList .row');
            ajaxCall(jsRoutes.controllers.Tasks.addTask(projectId, $(this).val()), function(data) {
                updateElement($(element), data);
                customAfterAjaxHandler();
                $('.taskList input.newTask').selectAll();
            });
        }
    });
    $('.task .taskbox').unbind('change');
    $('.task .taskbox').change(function(event) {
        var finished = $(this).is(':checked');
        var element = $(this);
        ajaxCall(jsRoutes.controllers.Tasks.updateTaskState($(this).data("task"), finished), function() {
            $(element).parents('.task').attr('finished', finished);
            customAfterAjaxHandler();
        });
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
    $('.task .taskTitle').unbind('click');
    $('.task .taskTitle').click(function(event) {
        $(this).siblings('.icon-edit').click();
        return false;
    });
    $('.task .icon.time').unbind('click');
    $('.task .icon.time').click(function(event) {
        $($(this).siblings('input[name="dueDate"]')).datepicker("show");
        return false;
    });
    $('.task input[name="dueDate"]').datepicker({
        changeMonth : false,
        changeYear : false,
        dateFormat: "dd.mm.yy" // @Messages("time.format.jquery") TODO i18n
    });
    $('.task input[name="dueDate"]').unbind("change");
    $('.task input[name="dueDate"]').change(function() {
        var element = $(this).parents('.task');
        var selectedTask = $(element).attr('id').replace('taskid_', '');
        updateTaskDueDate(element, selectedTask, $(this).val())
    });
    $('.task .btn.reset, .task button.reset').unbind('click');
    $('.task .btn.reset, .task button.reset').click(function() {
        clearModal($(this).parent('.modal.fade'));
    });
    $('.modal.fade').unbind('hide');
    $('.modal.fade').on('hide', function() {
        clearModal(this);
    });
    $('.modal.fade').unbind('shown');
    $('.modal.fade').on('shown', function() {
        $(this).find('.modal-body').find('textarea').focusEnd();
    });
    $('.task .btn.save').unbind('click');
    $('.task .btn.save').click(function() {
        var element = $(this).parents('.task');
        var selectedTask = $(element).attr('id').replace('taskid_', '');
        updateTaskDescription(element, selectedTask, $(this).parents('.modal.fade').find('.description-form textarea').val());
    });
}

function clearModal(element) {
    var textArea = $(element).find('.modal-body').find('textarea');
    $(textArea).val($(textArea).data('initial'));
}

$(function() {
    customAfterAjaxHandler();
});