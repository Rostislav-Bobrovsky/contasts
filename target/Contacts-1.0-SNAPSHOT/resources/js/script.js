function setIdsChecked(input) {
    var contactCheckboxes = document.getElementsByClassName('selectContact');
    var idsChecked = "";
    for (var i = 0; i < contactCheckboxes.length; ++i) {
        if (contactCheckboxes[i].checked == true) {
            idsChecked += contactCheckboxes[i].value + ",";
        }
    }
    idsChecked = idsChecked.substring(0, idsChecked.length - 1);
    document.getElementById('ids' + input).value = idsChecked;
    document.getElementById('selectAll').checked = false;
}

function chooseImage() {
    document.getElementById('inputPhoto').click();
}

function buttonDisabled() {
    var contactCheckboxes = document.getElementsByClassName('selectContact');
    var flag = false;
    for (var i = 0; i < contactCheckboxes.length; ++i) {
        if (contactCheckboxes[i].checked == true) {
            flag = true;
            break;
        }
    }

    if (flag) {
        document.getElementById('buttonRemove').disabled = false;
        document.getElementById('buttonSend').disabled = false;
    } else {
        document.getElementById('buttonRemove').disabled = true;
        document.getElementById('buttonSend').disabled = true;
    }
}

function onSelectAllClicked() {
    var contactCheckboxes = document.getElementsByClassName('selectContact');
    var selectAll = document.getElementById('selectAll');
    for (var i = 0; i < contactCheckboxes.length; ++i) {
        contactCheckboxes[i].checked = selectAll.checked;
    }

    buttonDisabled();
}

function onContactCheckboxClicked() {
    var contactCheckboxes = document.getElementsByClassName('selectContact');
    var selectAll = true;
    for (var i = 0; i < contactCheckboxes.length; ++i) {
        if (contactCheckboxes[i].checked != true) {
            selectAll = false;
            break;
        }
    }
    document.getElementById('selectAll').checked = selectAll;

    buttonDisabled();
}

function validateEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function fillFieldsPhonesAttachments() {
    var phoneCheckboxes = document.getElementsByClassName('selectPhone');
    var idsAllPhones = "";
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        idsAllPhones += phoneCheckboxes[i].value + '/';
    }
    document.getElementById('tablePhones').innerHTML += "<input id=\"idsAllPhones\"\
    name=\"idsAllPhones\" type=\"hidden\" value=\"" + idsAllPhones + "\">";

    var attachmentCheckboxes = document.getElementsByClassName('selectAttachment');
    var linesAllAttachments = "";
    for (var i = 0; i < attachmentCheckboxes.length; ++i) {
        linesAllAttachments += attachmentCheckboxes[i].value + '/';
    }
    document.getElementById('tableAttachments').innerHTML += "<input id=\"linesAllAttachments\"\
    name=\"linesAllAttachments\" type=\"hidden\" value=\"" + linesAllAttachments + "\">";
}

function checkValidDate(inputDate) {
    var verifiableDate = (document.getElementById(inputDate).value).trim();

    if (verifiableDate) {
        var inputDateSplit = verifiableDate.split("-");
        if (inputDateSplit.length != 3) {
            return false;
        }
        var date;
        if ((date = new Date(inputDateSplit[0] + '-' + inputDateSplit[1] + '-' + inputDateSplit[2])) == 'Invalid Date') {
            return false;
        } else if (inputDateSplit[2] != date.getDate()) {
            return false;
        }
        return true;
    }
    return true;
}

function checkEmptyFormAddEdit(addOrEdit) {
    var checkFirstName = (document.getElementById('firstName').value).trim();
    var checkLastName = (document.getElementById('firstName').value).trim();
    var checkEmail = (document.getElementById('inputEmail').value).trim();
    var checkCountry = (document.getElementById('inputCountry').value).trim();
    if (!checkLastName || !checkLastName || !validateEmail(checkEmail) || !checkCountry) {
        document.getElementById('lineBirthday').className = "form-group";
        if (!checkFirstName) {
            document.getElementById('lineFirstName').className = "has-error";
        } else {
            document.getElementById('lineFirstName').className = "";
        }
        if (!checkLastName) {
            document.getElementById('lineLastName').className = "has-error";
        } else {
            document.getElementById('lineLastName').className = "";
        }
        if (!validateEmail(checkEmail)) {
            document.getElementById('lineEmail').className = "form-group has-error";
        } else {
            document.getElementById('lineEmail').className = "form-group";
        }
        if (!checkCountry) {
            document.getElementById('lineCountry').className = "has-error";
        } else {
            document.getElementById('lineCountry').className = "";
        }
        return false;
    } else {
        document.getElementById('lineFirstName').className = "";
        document.getElementById('lineLastName').className = "";
        document.getElementById('lineEmail').className = "form-group";
        document.getElementById('lineEmail').className = "form-group";
    }

    var inputDate = (document.getElementById('inputDate').value).trim();
    if (!checkValidDate('inputDate')) {
        document.getElementById('lineBirthday').className = "form-group has-error";
        return false;
    } else {
        document.getElementById('lineBirthday').className = "form-group has-success";
    }

    document.getElementById('addOrEdit').value = addOrEdit;

    fillFieldsPhonesAttachments();
    return true;
}