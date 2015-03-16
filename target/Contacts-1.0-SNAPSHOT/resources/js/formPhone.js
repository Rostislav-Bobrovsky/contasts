function onSelectAllPhonesClicked() {
    var phoneCheckboxes = document.getElementsByClassName('selectPhone');
    var selectAllPhones = document.getElementById('selectAllPhones');
    for (var i = 0; i < phoneCheckboxes.length; ++i) {
        phoneCheckboxes[i].checked = selectAllPhones.checked;
    }
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
}

function setFieldsDefault() {
    document.getElementById('lineCountryCode').className = "";
    document.getElementById('lineOperatorCode').className = "";
    document.getElementById('linePhoneNumber').className = "";
}

function showFormPhoneAdd() {
    document.getElementById('popup-phone').style.display = "block";
    setFieldsDefault();
    document.getElementById('countryCode').value = null;
    document.getElementById('operatorCode').value = null;
    document.getElementById('phoneNumber').value = null;
    document.getElementById('commentPhone').value = null;
    document.getElementById('idFormPhone').value = null;
}

function showFormPhoneEdit(id) {
    document.getElementById('popup-phone').style.display = "block";
    setFieldsDefault();
    document.getElementById('countryCode').value = document.getElementById('inputCountryCode-' + id).value;
    document.getElementById('operatorCode').value = document.getElementById('inputOperatorCode-' + id).value;
    document.getElementById('phoneNumber').value = document.getElementById('inputPhoneNumber-' + id).value;
    document.getElementById('phoneType').value = document.getElementById('inputPhoneType-' + id).value;
    document.getElementById('commentPhone').value = document.getElementById('inputPhoneComment-' + id).value;
    document.getElementById('idFormPhone').value = id;
}

function hideFormPhone() {
    document.getElementById('popup-phone').style.display = "none";
}

var idForNewPhone = "new0";

function getIdNewPhone() {
    var id = idForNewPhone.substring(3, idForNewPhone.length);
    idForNewPhone = idForNewPhone.substring(0, 3) + (Number(id) + 1);
    return idForNewPhone;
}

function validateCountryCode(countryCode) {
    var re = /^([+]?[0-9]+)?$/;
    return re.test(countryCode);
}

function validateOperatorCode(operatorCode) {
    var re = /^([0-9]+)?$/;
    return re.test(operatorCode);
}

function validatePhoneNumber(phoneNumber) {
    var re = /^([0-9])+$/;
    return re.test(phoneNumber);
}

function savePhoneFormPhone() {
    var idExist = document.getElementById('idFormPhone').value;

    var countryCode = document.getElementById('countryCode').value;
    var operatorCode = document.getElementById('operatorCode').value;
    var phoneNumber = document.getElementById('phoneNumber').value;
    if (!validateCountryCode(countryCode) || !validateOperatorCode(operatorCode) || !validatePhoneNumber(phoneNumber)) {
        if (!validateCountryCode(countryCode)) {
            document.getElementById('lineCountryCode').className = "has-error";
        } else {
            document.getElementById('lineCountryCode').className = "";
        }
        if (!validateOperatorCode(operatorCode)) {
            document.getElementById('lineOperatorCode').className = "has-error";
        } else {
            document.getElementById('lineOperatorCode').className = "";
        }
        if (!validatePhoneNumber(phoneNumber)) {
            document.getElementById('linePhoneNumber').className = "has-error";
        } else {
            document.getElementById('linePhoneNumber').className = "";
        }
        return false;
    }

    if (!idExist) {
        var newId = getIdNewPhone();

        document.getElementById('tablePhones').innerHTML += "\
        <input type=\"hidden\" id=\"inputCountryCode-" + newId + "\" name=\"inputCountryCode-" + newId + "\" value=\"" + countryCode + "\">\
        <input type=\"hidden\" id=\"inputOperatorCode-" + newId + "\" name=\"inputOperatorCode-" + newId + "\" value=\"" + operatorCode + "\">\
        <input type=\"hidden\" id=\"inputPhoneNumber-" + newId + "\" name=\"inputPhoneNumber-" + newId + "\" value=\"" + phoneNumber + "\">\
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

        document.getElementById('popup-phone').style.display = "none";
        return true;
    } else {
        document.getElementById('inputCountryCode-' + idExist).value = countryCode;
        document.getElementById('inputOperatorCode-' + idExist).value = operatorCode;
        document.getElementById('inputPhoneNumber-' + idExist).value = phoneNumber;
        document.getElementById('inputPhoneType-' + idExist).value = document.getElementById('phoneType').value;
        document.getElementById('inputPhoneComment-' + idExist).value = document.getElementById('commentPhone').value;

        document.getElementById('viewFullPhoneNumber-' + idExist).innerHTML =
            document.getElementById('inputCountryCode-' + idExist).value +
            " (" + document.getElementById('inputOperatorCode-' + idExist).value + ") " +
            document.getElementById('inputPhoneNumber-' + idExist).value;
        document.getElementById('viewPhoneType-' + idExist).innerHTML = document.getElementById('inputPhoneType-' + idExist).value;
        document.getElementById('viewPhoneComment-' + idExist).innerHTML = document.getElementById('inputPhoneComment-' + idExist).value;

        document.getElementById('popup-phone').style.display = "none";
        return true;
    }
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
    document.getElementById('selectAllPhones').checked = false;
}