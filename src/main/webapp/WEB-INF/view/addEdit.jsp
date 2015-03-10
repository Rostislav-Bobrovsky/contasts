<%@ page import="com.itechart.contacts.model.PhoneType" %>
<%@ page import="com.itechart.contacts.model.RelationshipStatus" %>
<%@ page import="com.itechart.contacts.model.Sex" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>${buttonForm}</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
</head>
<body>
<div class="container">
    <header class="page-header">
        <h1 class="text-center">${buttonForm}</h1>
    </header>

    <form action="${pageContext.request.contextPath}/contacts" method="post">
        <div class="row">
            <div class="col-lg-4">
                <div class="row">
                    <input type="hidden" name="idPeople" value="${people.id}">
                    <div class="col-lg-12">
                        <div class="col-lg-4">
                            <br>
                            <img src="${pageContext.request.contextPath}/resources/img/UserProfile2.png" height="110"
                                 width="110"
                                 alt="Profile photo" class="img-rounded">
                        </div>
                        <div class="col-lg-8">
                            <input type="text" class="form-control" id="firstName" name="firstName"
                                   placeholder="First Name"
                                   value="${people.firstName}">
                            <br>
                            <input type="text" class="form-control" id="lastName" name="lastName"
                                   placeholder="Last Name"
                                   value="${people.lastName}">
                            <br>
                            <input type="text" class="form-control" id="surName" name="surName" placeholder="Surname"
                                   value="${people.surName}">
                            <br>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputDate" class="col-lg-4 control-label">Birthday</label>

                                <div class="col-lg-8">
                                    <input class="form-control" type="text" name="inputDate" id="inputDate"
                                           placeholder="yyyy-MM-dd"
                                           value="${people.birthday}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputSex" class="col-lg-4 control-label">Sex</label>

                                <div class="col-lg-8">
                                    <select id="inputSex" name="inputSex" class="form-control">
                                        <c:if test="${not empty people.sex}">
                                            <option selected>${people.sex.displayName}</option>
                                        </c:if>
                                        <c:forEach var="sex" items="<%=Sex.values()%>">
                                            <c:if test="${not sex.equals(people.sex)}">
                                                <option>${sex.displayName}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputNationality" class="col-lg-4 control-label">Nationality</label>

                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inputNationality"
                                           name="inputNationality"
                                           placeholder="Nationality"
                                           value="${people.nationality}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="relationshipStatus" class="col-lg-4 control-label">Relationship
                                    status</label>

                                <div class="col-lg-8">
                                    <select id="relationshipStatus" name="relationshipStatus" class="form-control">
                                        <c:if test="${not empty people.relationshipStatus}">
                                            <option selected>${people.relationshipStatus.displayName}</option>
                                        </c:if>
                                        <c:forEach var="relationshipStatus" items="<%=RelationshipStatus.values()%>">
                                            <c:if test="${not relationshipStatus.equals(people.relationshipStatus)}">
                                                <option>${relationshipStatus.displayName}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputWebSite" class="col-lg-4 control-label">Web Site</label>

                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inputWebSite" name="inputWebSite"
                                           placeholder="Web Site"
                                           value="${people.webSite}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputEmail" class="col-lg-4 control-label">Email</label>

                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inputEmail" name="inputEmail"
                                           placeholder="Email"
                                           value="${people.email}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputJob" class="col-lg-4 control-label">Job</label>

                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inputJob" name="inputJob"
                                           placeholder="Job"
                                           value="${people.job}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputCountry" class="col-lg-4 control-label">Region</label>

                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="inputCountry" name="inputCountry"
                                           placeholder="Country"
                                           value="${people.address.country}">
                                </div>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="inputCity" name="inputCity"
                                           placeholder="City"
                                           value="${people.address.city}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputStreet" class="col-lg-4 control-label">Address</label>

                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inputStreet" name="inputStreet"
                                           placeholder="Street"
                                           value="${people.address.street}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputHouse" class="col-lg-4 control-label"></label>

                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="inputHouse" name="inputHouse"
                                           placeholder="House"
                                           value="${people.address.house}">
                                </div>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="inputApartment" name="inputApartment"
                                           placeholder="Apartment"
                                           value="${people.address.apartment}">
                                </div>
                            </div>
                        </div>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="inputIndex" class="col-lg-4 control-label">Index</label>

                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inputIndex" name="inputIndex"
                                           placeholder="Index"
                                           value="${people.address.index}">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="row pull-right">
                    <button type="button" onclick="showFormPhoneAdd();" class="btn btn-success">Add</button>
                    <button type="button" onclick="removePhones('');" class="btn btn-danger">Remove</button>
                </div>
                <br><br>

                <div class="row">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th><input id="selectAllPhones" onclick="onSelectAllPhonesClicked();" type="checkbox"></th>
                            <th>Phone Number</th>
                            <th>Type</th>
                            <th>Comment</th>
                        </tr>
                        </thead>
                        <tbody id="tablePhones">
                        <c:forEach var="phone" items="${people.phones}">

                            <input type="hidden" id="inputCountryCode-${phone.id}" name="inputCountryCode-${phone.id}"
                                   value="${phone.countryCode}">
                            <input type="hidden" id="inputOperatorCode-${phone.id}" name="inputOperatorCode-${phone.id}"
                                   value="${phone.operatorCode}">
                            <input type="hidden" id="inputPhoneNumber-${phone.id}" name="inputPhoneNumber-${phone.id}"
                                   value="${phone.phoneNumber}">
                            <input type="hidden" id="inputPhoneType-${phone.id}" name="inputPhoneType-${phone.id}"
                                   value="${phone.phoneType.displayName}">
                            <input type="hidden" id="inputPhoneComment-${phone.id}" name="inputPhoneComment-${phone.id}"
                                   value="${phone.comment}">

                            <tr id="line-${phone.id}">
                                <td id="viewCountryCode-${phone.id}"><input class="selectPhone"
                                                                            onclick="onPhoneCheckboxClicked();"
                                                                            type="checkbox" value="${phone.id}"></td>
                                <td><a id="viewFullPhoneNumber-${phone.id}" onclick="showFormPhoneEdit('${phone.id}');"
                                       href="javascript:void(0)">${phone.countryCode}
                                    (${phone.operatorCode}) ${phone.phoneNumber}</a>
                                </td>
                                <td id="viewPhoneType-${phone.id}">${phone.phoneType.displayName}</td>
                                <td id="viewPhoneComment-${phone.id}">${phone.comment}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <nav class="pull-right">
                        <ul class="pagination">
                            <li>
                                <a href="#" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                            <li><a href="#">1</a></li>
                            <li>
                                <a href="#" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
                <br><br>

                <div class="row pull-right">
                    <a class="btn btn-success" onclick="showFormAttachmentAdd();" href="#">Add</a>
                    <a class="btn btn-danger" href="#">Remove</a>
                </div>
                <br><br>

                <div class="row">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th><input type="checkbox"></th>
                            <th>Filename</th>
                            <th>Uploads</th>
                            <th>Comment</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="attachment" items="">
                            <tr>
                                <td><input type="checkbox"></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <nav class="pull-right">
                        <ul class="pagination">
                            <li>
                                <a href="#" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                            <li><a href="#">1</a></li>
                            <li>
                                <a href="#" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            <input id="addOrEdit" name="addOrEdit" type="hidden"/>
        </div>
        <div class="row">
            <div class="col-lg-6">
                <a class="btn btn-default btn-lg pull-right" href="http://localhost:8080/contacts">Cancel</a>
            </div>
            <div class="col-lg-6">
                <button type="submit" class="btn btn-success btn-lg"
                        onclick="checkEmptyFormAddEdit('${buttonForm}');">${buttonForm} contact
                </button>
            </div>
        </div>
    </form>

    <div id="popup-phone">
        <div class="popup-form">
            <form action="#" class="form-settings" name="form">
                <img class="close" src="${pageContext.request.contextPath}/resources/img/close.png"
                     onclick="hideFormPhone()">

                <h2 class="text-center">Phone</h2>
                <hr class="hr-line">
                <input id="idFormPhone" type="hidden">
                <input class="form-control" id="countryCode" placeholder="Country code" type="text">
                <br>
                <input class="form-control" id="operatorCode" placeholder="Operator code" type="text">
                <br>
                <input class="form-control" id="phoneNumber" placeholder="Phone number" type="text">
                <br>
                <select id="phoneType" class="form-control">
                    <c:forEach var="phoneType" items="<%=PhoneType.values()%>">
                        <option>${phoneType.displayName}</option>
                    </c:forEach>
                </select>
                <br>
                <textarea class="form-control textarea-vertical" id="commentPhone"
                          placeholder="Comment"></textarea>
                <br>
                <a class="btn btn-success center-block" onclick="savePhoneFormPhone('${idLastPhone}');"
                   href="#">Save</a>
            </form>
        </div>
    </div>
    <div id="popup-attachment">
        <div class="popup-form">
            <form action="#" class="form-settings" name="form">
                <img class="close" src="${pageContext.request.contextPath}/resources/img/close.png"
                     onclick="hideFormAttachment()">

                <h2 class="text-center">Attachment</h2>
                <hr class="hr-line">
                <input id="Filename" placeholder="Filename" type="file">
                <br>
                <textarea class="form-control textarea-vertical" id="commentAttachment"
                          placeholder="Comment"></textarea>
                <br>
                <a class="btn btn-success center-block" href="#">Save</a>
            </form>
        </div>
    </div>

</div>

</body>
</html>
