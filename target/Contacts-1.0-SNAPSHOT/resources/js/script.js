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

function onSelectAllPhonesClicked() {
    var phoneCheckboxes = document.getElementsByClassName('selectPhone');
    var selectAllPhones = document.getElementById('selectAllPhones');
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        phoneCheckboxes[i].checked = selectAllPhones.checked;
    }
    //setIdsPhoneChecked();
}

function onPhoneCheckboxClicked() {
    var phoneCheckboxes = document.getElementsByClassName('selectPhone');
    var selectAllPhones = true;
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        if (phoneCheckboxes[i].checked != true) {
            selectAllPhones = false;
            break;
        }
    }
    document.getElementById('selectAllPhones').checked = selectAllPhones;

    //setIdsPhoneChecked();
}

var idForNewPhone = "new0";

function getIdNewPhone() {
    var id = idForNewPhone.substring(3, idForNewPhone.length);
    idForNewPhone = idForNewPhone.substring(0, 3) + (Number(id) + 1);
    return idForNewPhone;
}

function savePhoneFormPhone() {
    var idExist = document.getElementById('idFormPhone').value;
    if (!document.getElementById('phoneNumber').value) {
        alert("Fill Phone number!");
    } else if (!idExist) {
        var newId = getIdNewPhone();

        document.getElementById('tablePhones').innerHTML += "\
        <input type=\"hidden\" id=\"inputCountryCode-" + newId + "\" name=\"inputCountryCode-" + newId + "\" value=\"" + document.getElementById('countryCode').value + "\">\
        <input type=\"hidden\" id=\"inputOperatorCode-" + newId + "\" name=\"inputOperatorCode-" + newId + "\" value=\"" + document.getElementById('operatorCode').value + "\">\
        <input type=\"hidden\" id=\"inputPhoneNumber-" + newId + "\" name=\"inputPhoneNumber-" + newId + "\" value=\"" + document.getElementById('phoneNumber').value + "\">\
        <input type=\"hidden\" id=\"inputPhoneType-" + newId + "\" name=\"inputPhoneType-" + newId + "\" value=\"" + document.getElementById('phoneType').value + "\">\
        <input type=\"hidden\" id=\"inputPhoneComment-" + newId + "\" name=\"inputPhoneComment-" + newId + "\" value=\"" + document.getElementById('commentPhone').value + "\">\
        <tr  id=\"line-" + newId + "\">\
        <td id=\"viewCountryCode-" + newId + "\"><input class=\"selectPhone\" onclick=\"onPhoneCheckboxClicked();\" type=\"checkbox\" value=\"" + newId + "\"></td>\
        <td><a id=\"viewFullPhoneNumber-" + newId + "\" onclick=\"showFormPhoneEdit('" + newId + "');\"href=\"javascript:void(0)\"></a></td>\
        <td id=\"viewPhoneType-" + newId + "\"></td>\
        <td id=\"viewPhoneComment-" + newId + "\"></td>\
        </tr>";

        document.getElementById('viewFullPhoneNumber-' + newId).innerHTML = document.getElementById('inputCountryCode-' + newId).value + " (" + document.getElementById('inputOperatorCode-' + newId).value + ") " + document.getElementById('inputPhoneNumber-' + newId).value;
        document.getElementById('viewPhoneType-' + newId).innerHTML = document.getElementById('inputPhoneType-' + newId).value;
        document.getElementById('viewPhoneComment-' + newId).innerHTML = document.getElementById('inputPhoneComment-' + newId).value;

    } else {
        document.getElementById('inputCountryCode-' + idExist).value = document.getElementById('countryCode').value;
        document.getElementById('inputOperatorCode-' + idExist).value = document.getElementById('operatorCode').value;
        document.getElementById('inputPhoneNumber-' + idExist).value = document.getElementById('phoneNumber').value;
        document.getElementById('inputPhoneType-' + idExist).value = document.getElementById('phoneType').value;
        document.getElementById('inputPhoneComment-' + idExist).value = document.getElementById('commentPhone').value;

        document.getElementById('viewFullPhoneNumber-' + idExist).innerHTML =
            document.getElementById('inputCountryCode-' + idExist).value +
            " (" + document.getElementById('inputOperatorCode-' + idExist).value + ") " +
            document.getElementById('inputPhoneNumber-' + idExist).value;
        document.getElementById('viewPhoneType-' + idExist).innerHTML = document.getElementById('inputPhoneType-' + idExist).value;
        document.getElementById('viewPhoneComment-' + idExist).innerHTML = document.getElementById('inputPhoneComment-' + idExist).value;
    }

    document.getElementById('popup-phone').style.display = "none";
}

function checkEmptyFormAddEdit(addOrEdit) {
    if (!((document.getElementById('firstName').value).trim()) || !((document.getElementById('lastName').value).trim()) || !((document.getElementById('inputEmail').value).trim()) || !((document.getElementById('inputCountry').value).trim())) {
        alert("Fill \nFirst Name \nLast Name \nInput Email \nInput Country");
    }

    var inputDate = (document.getElementById('inputDate').value).trim();

    var inputDateSplit = inputDate.split("-");
    if (inputDateSplit.length != 3) {
        alert("Invalid Date input");
    }
    var date;
    if ((date = new Date(inputDateSplit[0] + '-' + inputDateSplit[1] + '-' + inputDateSplit[2])) == 'Invalid Date') {
        alert('Invalid Date not correct')
    } else if (inputDateSplit[2] != date.getDate()) {
        alert('not exist');
    }

    document.getElementById('addOrEdit').value = addOrEdit;

    var phoneCheckboxes = document.getElementsByClassName('selectPhone');
    var idsAllPhones = "";
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        idsAllPhones += phoneCheckboxes[i].value + '/';
    }
    document.getElementById('tablePhones').innerHTML += "<input id=\"idsAllPhones\"\
    name=\"idsAllPhones\" type=\"hidden\" value=\"" + idsAllPhones + "\">";
}

function showFormPhoneAdd() {
    document.getElementById('popup-phone').style.display = "block";

    document.getElementById('countryCode').value = null;
    document.getElementById('operatorCode').value = null;
    document.getElementById('phoneNumber').value = null;
    document.getElementById('commentPhone').value = null;
    document.getElementById('idFormPhone').value = null;
}

function showFormPhoneEdit(id) {
    document.getElementById('popup-phone').style.display = "block";
    document.getElementById('countryCode').value = document.getElementById('inputCountryCode-' + id).value;
    document.getElementById('operatorCode').value = document.getElementById('inputOperatorCode-' + id).value;
    document.getElementById('phoneNumber').value = document.getElementById('inputPhoneNumber-' + id).value;
    document.getElementById('phoneType').value = document.getElementById('inputPhoneType-' + id).value;
    document.getElementById('commentPhone').value = document.getElementById('inputPhoneComment-' + id).value;
    document.getElementById('idFormPhone').value = id;
}

function removePhones() {
    var phoneCheckboxes = document.getElementsByClassName('selectPhone');
    var idsPhones = [];
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        if (phoneCheckboxes[i].checked == true) {
            idsPhones.push(phoneCheckboxes[i].value);
        }
    }
    for (var i = 0; i < idsPhones.length; ++i) {
        var id = idsPhones[i];

        var countryCode = document.getElementById("inputCountryCode-" + id);
        countryCode.parentNode.removeChild(countryCode);
        var operatorCode = document.getElementById("inputOperatorCode-" + id);
        operatorCode.parentNode.removeChild(operatorCode);
        var phoneNumber = document.getElementById("inputPhoneNumber-" + id);
        phoneNumber.parentNode.removeChild(phoneNumber);
        var phoneType = document.getElementById("inputPhoneType-" + id);
        phoneType.parentNode.removeChild(phoneType);
        var phoneComment = document.getElementById("inputPhoneComment-" + id);
        phoneComment.parentNode.removeChild(phoneComment);
        var line = document.getElementById("line-" + id);
        line.parentNode.removeChild(line);
    }
}

function hideFormPhone() {
    document.getElementById('popup-phone').style.display = "none";

}

function showFormAttachmentAdd() {
    document.getElementById('popup-attachment').style.display = "block";
}

function hideFormAttachment() {
    document.getElementById('popup-attachment').style.display = "none";
}

function checkValidDate(inputDateId) {
    var inputDate = (document.getElementById(inputDateId).value).trim();

    var inputDateSplit = inputDate.split("-");
    if (inputDateSplit.length != 3) {
        return false;
    }
    var date;
    if ((date = new Date(inputDateSplit[0] + '-' + inputDateSplit[1] + '-' + inputDateSplit[2])) == 'Invalid Date') {
        return false;
    } else if (inputDateSplit[2] != date.getDate()) {
        return true;
    }
}


function checkSearchForm() {
    if ((document.getElementById('inputDateFrom')).value.trim()) {
        if (!checkValidDate('inputDateFrom')) {
            alert('inputDateFrom ---');
        }
    }
    if ((document.getElementById('inputDateTo')).value.trim()) {
        if (!checkValidDate('inputDateTo')) {
            alert('inputDateTo ---');
        }
    }
    if (!((document.getElementById('inputFirstName')).value.trim()) || !((document.getElementById('inputLastName')).value.trim()) || !((document.getElementById('inputSurName')).value.trim()) || !((document.getElementById('inputDateFrom')).value.trim()) || !((document.getElementById('inputDateTo')).value.trim()) || !((document.getElementById('inputNationality')).value.trim()) || !((document.getElementById('inputWebSite')).value.trim()) || !((document.getElementById('inputEmail')).value.trim()) || !((document.getElementById('inputJob')).value.trim()) || !((document.getElementById('inputCountry')).value.trim()) || !((document.getElementById('inputCity')).value.trim()) || !((document.getElementById('inputStreet')).value.trim()) || !((document.getElementById('inputHouse')).value.trim()) || !((document.getElementById('inputApartment')).value.trim()) || !((document.getElementById('inputIndex')).value.trim())) {
        alert('Form is empty');
    }
}

function checkSendForm() {
    if (!((document.getElementById('textareaText').innerHTML).trim())) {
        alert('Enter Compose Email');
    }
}


function changeTemplate() {
    if ('-None selected-' == document.getElementById('templateSelect').value) {
        document.getElementById('textareaText').disabled = false;
        document.getElementById('textareaText').innerHTML = "";
    } else {
        document.getElementById('textareaText').disabled = true;
        document.getElementById('textareaText').innerHTML = document.getElementById('id' + document.getElementById('templateSelect').value).value;
    }
}