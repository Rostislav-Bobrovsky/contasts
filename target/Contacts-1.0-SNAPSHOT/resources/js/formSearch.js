function checkSearchForm() {
    if ((document.getElementById('inputDateFrom')).value.trim()) {
        if (!checkValidDate('inputDateFrom')) {
            document.getElementById('messageError').className = "hidden";
            document.getElementById('lineDateFrom').className = "has-error";
            return false;
        } else {
            document.getElementById('lineDateFrom').className = "";
        }
    } else {
        document.getElementById('lineDateFrom').className = "";
    }
    if ((document.getElementById('inputDateTo')).value.trim()) {
        if (!checkValidDate('inputDateTo')) {
            document.getElementById('messageError').className = "hidden";
            document.getElementById('lineDateTo').className = "has-error";
            return false;
        } else {
            document.getElementById('lineDateTo').className = "";
        }
    } else {
        document.getElementById('lineDateTo').className = "";
    }
    if (!((document.getElementById('inputFirstName')).value.trim()) &&
        !((document.getElementById('inputLastName')).value.trim()) &&
        !((document.getElementById('inputSecondName')).value.trim()) &&
        !((document.getElementById('inputDateFrom')).value.trim()) &&
        !((document.getElementById('inputDateTo')).value.trim()) &&
        '-None selected-' == (document.getElementById('inputSex')).value &&
        !((document.getElementById('inputNationality')).value.trim()) &&
        '---' == (document.getElementById('relationshipStatus')).value &&
        !((document.getElementById('inputWebSite')).value.trim()) &&
        !((document.getElementById('inputEmail')).value.trim()) &&
        !((document.getElementById('inputJob')).value.trim()) &&
        !((document.getElementById('inputCountry')).value.trim()) &&
        !((document.getElementById('inputCity')).value.trim()) &&
        !((document.getElementById('inputStreet')).value.trim()) &&
        !((document.getElementById('inputHouse')).value.trim()) &&
        !((document.getElementById('inputApartment')).value.trim()) &&
        !((document.getElementById('inputIndex')).value.trim())) {

        document.getElementById('messageError').className = "row";
        document.getElementById('lineDateFrom').className = "";
        document.getElementById('lineDateTo').className = "";
        return false;
    }
    return true;
}