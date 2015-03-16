<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contact List</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
</head>
<body>
<div class="container">
    <header class="page-header">
        <a href="list?limit=${limit}&offset=0"><h1 class="text-center">Contact List</h1></a>
    </header>
    <br><br>

    <div class="table">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th><input id="selectAll" onclick="onSelectAllClicked();" type="checkbox"></th>
                <th>Full Name</th>
                <th>Birthday</th>
                <th>Address</th>
                <th>Job</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="people" items="${peoples}">
                <tr>
                    <td><input class="selectContact" onclick="onContactCheckboxClicked();" type="checkbox"
                               value="${people.id}"></td>
                    <td>
                        <a href="editExist?action=editExist&id=${people.id}&limit=${limit}&offset=${offset}">${people.firstName} ${people.lastName} ${people.secondName}</a>
                    </td>
                    <td>${people.birthday}</td>
                    <td>${people.address.country} ${people.address.city} ${people.address.street} ${people.address.house} ${people.address.apartment} ${people.address.index}</td>
                    <td>${people.job}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="row">
        <div class="col-lg-2">
            <a class="btn btn-success center-block"
               href="${pageContext.request.contextPath}/contacts/addNew?action=addNew&limit=${limit}&offset=${offset}"><span
                    class="glyphicon glyphicon-plus-sign"></span>&nbsp;Add</a>
        </div>
        <div class="col-lg-2">
            <form action="list?action=remove&limit=${limit}&offset=${offset}" method="post">
                <input id="idsRemove" name="idsRemove" type="hidden">
                <button type="submit" id="buttonRemove" onclick="setIdsChecked('Remove')"
                        class="btn btn-danger center-block form-control" disabled><span
                        class="glyphicon glyphicon-trash"></span>&nbsp;Remove
                </button>
            </form>
        </div>
        <div class="col-lg-offset-4 col-lg-2">
            <form action="send?action=send&limit=${limit}&offset=${offset}" method="post">
                <input id="idsSend" name="idsSend" type="hidden">
                <button type="submit" id="buttonSend" onclick="setIdsChecked('Send')"
                        class="btn btn-primary center-block form-control" disabled><span
                        class="glyphicon glyphicon-send"></span>&nbsp;Send
                </button>
            </form>
        </div>
        <div class="col-lg-2">
            <a class="btn btn-info center-block" href="list?action=search&limit=${limit}&offset=${offsetRight}"><span
                    class="glyphicon glyphicon-search"></span>&nbsp;Search</a>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-1">
            <br>
            <p class="text-muted text-right">Show</p>
        </div>
        <div class="col-lg-1">
            <br>
            <select id="limitSelect" class="form-control input-sm" onchange="window.location.href = this.value;">
                <c:choose>
                    <c:when test="${limit == 10}">
                        <option value="list?limit=10&offset=0">10</option>
                        <option value="list?limit=20&offset=0">20</option>
                    </c:when>
                    <c:otherwise>
                        <option value="list?limit=10&offset=0">10</option>
                        <option selected value="list?limit=20&offset=0">20</option>
                    </c:otherwise>
                </c:choose>
            </select>
        </div>
        <div class="col-lg-1">
            <br>
            <p class="text-muted">elements</p>
        </div>
        <div class="col-lg-offset-1 col-lg-3">
            <br>
            <p class="text-muted text-right">Showing ${offset * limit + 1} to ${(offset + 1) * limit} of ${all} entries</p>
        </div>
        <div class="col-lg-offset-3 col-lg-2">
            <nav class="pull-right">
                <ul class="pagination">
                    <li>
                        <a href="list?limit=${limit}&offset=${offsetLeft}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li><a href="javascript:void(0)">${offset + 1}</a></li>
                    <li>
                        <a href="list?limit=${limit}&offset=${offsetRight}"
                           aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>
</body>
</html>