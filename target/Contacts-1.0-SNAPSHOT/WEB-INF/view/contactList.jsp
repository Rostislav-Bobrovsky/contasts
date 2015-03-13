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
        <a href="${pageContext.request.contextPath}/contacts/1?limit=10&offset=0"><h1 class="text-center">Contact List</h1></a>
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
                        <a href="${pageContext.request.contextPath}/contacts/edit/${people.id}">${people.firstName} ${people.lastName} ${people.surName}</a>
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
        <div class="col-lg-1">
            <a class="btn btn-success center-block" href="${pageContext.request.contextPath}/contacts/add">Add</a>
        </div>
        <div class="col-lg-1">
            <form action="${pageContext.request.contextPath}/contacts/remove" method="post">
                <input id="idsRemove" name="idsRemove" type="hidden">
                <button type="submit" id="buttonRemove" onclick="setIdsChecked('Remove')"
                        class="btn btn-danger center-block" disabled>Remove
                </button>
            </form>
        </div>
        <div class="col-lg-offset-8 col-lg-1">
            <form action="${pageContext.request.contextPath}/contacts/send" method="post">
                <input id="idsSend" name="idsSend" type="hidden">
                <button type="submit" id="buttonSend" onclick="setIdsChecked('Send')"
                        class="btn btn-primary center-block " disabled>Send
                </button>
            </form>
        </div>
        <div class="col-lg-1">
            <a class="btn btn-info center-block" href="${pageContext.request.contextPath}/contacts/search">Search</a>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <br>
            <label>
                Show
                <select id="limitSelect" class="input-sm" onchange="window.location.href = this.value;">
                    <c:choose>
                        <c:when test="${limit == 10}">
                            <option value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=10&offset=0">10</option>
                            <option value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=20&offset=0">20</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=10&offset=0">10</option>
                            <option selected value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=20&offset=0">20</option>
                        </c:otherwise>
                    </c:choose>
                    <%--<c:if test="${limit == 10}">--%>
                        <%--<option value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=10&offset=0">10</option>--%>
                        <%--<option value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=20&offset=0">20</option>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${limit == 20}">--%>
                        <%--<option value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=10&offset=0">10</option>--%>
                        <%--<option selected value="${pageContext.request.contextPath}/contacts/${tablePage}?limit=20&offset=0">20</option>--%>
                    <%--</c:if>--%>
                </select>
                entries
            </label>
        </div>
        <div class="col-lg-6">
            <nav class="pull-right">
                <ul class="pagination">
                    <li>
                        <a href="${pageContext.request.contextPath}/contacts/${tablePage}?limit=${limit}&offset=${offsetLeft}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li><a  href="javascript:void(0)">${tablePage}</a></li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contacts/${tablePage}?limit=${limit}&offset=${offsetRight}"
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