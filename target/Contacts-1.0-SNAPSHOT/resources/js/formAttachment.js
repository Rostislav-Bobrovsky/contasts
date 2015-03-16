function onSelectAllAttachmentsClicked() {
    var phoneCheckboxes = document.getElementsByClassName('selectAttachment');
    var selectAllPhones = document.getElementById('selectAllAttachments');
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        phoneCheckboxes[i].checked = selectAllPhones.checked;
    }
}

function onAttachmentCheckboxClicked() {
    var phoneCheckboxes = document.getElementsByClassName('selectAttachment');
    var selectAllPhones = true;
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        if (phoneCheckboxes[i].checked != true) {
            selectAllPhones = false;
            break;
        }
    }
    document.getElementById('selectAllAttachments').checked = selectAllPhones;
}

function saveAttachmentFormPhone() {
    var newLine = Number(document.getElementById('numberLastAttachment').value) + 1;
    var numberCurrentLine = document.getElementById('lineEditAttachment').value;
    if (!numberCurrentLine) {
        var fullPath = document.getElementById('inputAttachmentFile-' + newLine).value;
        if (!fullPath) {
            alert("Upload file!");
            return false;
        }

        var filenameOnly;
        var fileType;
        var uploadDate;
        var comment;

        var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
        var filename = fullPath.substring(startIndex);
        if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
            filename = filename.substring(1);
        }
        startIndex = filename.lastIndexOf('.');
        fileType = filename.substring(startIndex);

        var newNameAttachment = document.getElementById('nameAttachment').value.trim();
        if (!newNameAttachment) {
            filenameOnly = filename.substring(0, startIndex);
        } else {
            filenameOnly = newNameAttachment;
        }
        var today = new Date();
        uploadDate = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
        comment = document.getElementById('commentAttachment').value.trim();

        document.getElementById('tableAttachments').innerHTML += "\
        <input type=\"hidden\" id=\"inputAttachmentId-" + newLine + "\" name=\"inputAttachmentId-" + newLine + "\" value=\"-1\">\
        <input type=\"hidden\" id=\"inputAttachmentName-" + newLine + "\" name=\"inputAttachmentName-" + newLine + "\" value=\"" + filenameOnly + "\">\
        <input type=\"hidden\" id=\"inputAttachmentType-" + newLine + "\" name=\"inputAttachmentType-" + newLine + "\" value=\"" + fileType + "\">\
        <input type=\"hidden\" id=\"inputAttachmentDate-" + newLine + "\" name=\"inputAttachmentDate-" + newLine + "\" value=\"" + uploadDate + "\">\
        <input type=\"hidden\" id=\"inputAttachmentComment-" + newLine + "\" name=\"inputAttachmentComment-" + newLine + "\" value=\"" + comment + "\">\
        <tr id=\"lineAttachment-" + newLine + "\">\
        <td id=\"viewAttachment-" + newLine + "\"><input type=\"checkbox\" value=\"" + newLine + "\" class=\"selectAttachment\" onclick=\"onAttachmentCheckboxClicked();\"></td>\
        <td><a id=\"viewAttachmentName-" + newLine + "\" onclick=\"showFormAttachmentEdit(" + newLine + ");\" href=\"javascript:void(0)\"></a></td>\
        <td id=\"viewAttachmentDate-" + newLine + "\"></td>\
        <td id=\"viewAttachmentComment-" + newLine + "\"></td>\
        </tr>";

        document.getElementById('viewAttachmentName-' + newLine).innerHTML = filenameOnly;
        document.getElementById('viewAttachmentDate-' + newLine).innerHTML = uploadDate;
        document.getElementById('viewAttachmentComment-' + newLine).innerHTML = comment;

        document.getElementById('numberLastAttachment').value = newLine;
        if (newLine == document.getElementById('maxUpload').value) {
            var add = document.getElementById("addAttachment");
            add.parentNode.removeChild(add);
        }
    } else {
        var attachmentName = document.getElementById('nameAttachment').value.trim();
        var attachmentComment = document.getElementById('commentAttachment').value.trim();

        if (attachmentName) {
            document.getElementById('inputAttachmentName-' + numberCurrentLine).value = attachmentName;
            document.getElementById('viewAttachmentName-' + numberCurrentLine).innerHTML = attachmentName;
        }

        document.getElementById('inputAttachmentComment-' + numberCurrentLine).value = attachmentComment;
        document.getElementById('viewAttachmentComment-' + numberCurrentLine).innerHTML = attachmentComment;
    }

    document.getElementById('popup-attachment').style.display = "none";
    return true;
}

function showFormAttachmentAdd() {
    document.getElementById('popup-attachment').style.display = "block";

    document.getElementById('buttonUpload').disabled = false;
    document.getElementById('lineEditAttachment').value = null;
    document.getElementById('nameAttachment').value = null;
    document.getElementById('commentAttachment').value = null;
}

function showFormAttachmentEdit(lineNumber) {
    document.getElementById('popup-attachment').style.display = "block";

    document.getElementById('buttonUpload').disabled = true;
    document.getElementById('lineEditAttachment').value = lineNumber;
    document.getElementById('nameAttachment').value = "";
    document.getElementById('commentAttachment').value = document.getElementById('inputAttachmentComment-' + lineNumber).value;
}

function removeAttachments() {
    var attachmentsCheckboxes = document.getElementsByClassName('selectAttachment');
    var linesAttachments = [];
    for (var i = 0; i < attachmentsCheckboxes.length; ++i) {
        if (attachmentsCheckboxes[i].checked == true) {
            linesAttachments.push(attachmentsCheckboxes[i].value);
        }
    }
    for (var i = 0; i < linesAttachments.length; ++i) {
        var line = linesAttachments[i];

        if (document.getElementById("inputAttachmentId-" + line).value != -1) {
            document.getElementById('removesFiles').value += document.getElementById("inputAttachmentNameGenerated-" + line).value + '/';
            var generatedName = document.getElementById("inputAttachmentNameGenerated-" + line);
            generatedName.parentNode.removeChild(generatedName);
        } else {
            var file = document.getElementById("inputAttachmentFile-" + line);
            file.parentNode.removeChild(file);
        }
        var id = document.getElementById("inputAttachmentId-" + line);
        id.parentNode.removeChild(id);
        var name = document.getElementById("inputAttachmentName-" + line);
        name.parentNode.removeChild(name);
        var type = document.getElementById("inputAttachmentType-" + line);
        type.parentNode.removeChild(type);
        var date = document.getElementById("inputAttachmentDate-" + line);
        date.parentNode.removeChild(date);
        var comment = document.getElementById("inputAttachmentComment-" + line);
        comment.parentNode.removeChild(comment);
        var lineAttachment = document.getElementById("lineAttachment-" + line);
        lineAttachment.parentNode.removeChild(lineAttachment);
    }
    document.getElementById('selectAllAttachments').checked = false;
}

function chooseFile() {
    document.getElementById('inputAttachmentFile-' + (Number(document.getElementById('numberLastAttachment').value) + 1)).click();
}

function hideFormAttachment() {
    document.getElementById('popup-attachment').style.display = "none";
}