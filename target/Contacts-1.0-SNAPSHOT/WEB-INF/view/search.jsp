<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.itechart.contacts.model.RelationshipStatus" %>
<%@ page import="com.itechart.contacts.model.Sex" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
</head>
<body>
<div class="container">
    <header class="page-header">
        <h1 class="text-center">Search</h1>
    </header>
    <form action="${pageContext.request.contextPath}/contacts" method="post">
        <div class="row">
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputFirstName" class="col-lg-3 control-label">First Name</label>

                    <div class="col-lg-9">
                        <input type="text" class="form-control" id="inputFirstName" name="inputFirstName"
                               placeholder="First Name">
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputLastName" class="col-lg-3 control-label">Last Name</label>

                    <div class="col-lg-9">
                        <input type="text" class="form-control" id="inputLastName" name="inputLastName"
                               placeholder="Last Name">
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputSurName" class="col-lg-3 control-label">Surname</label>

                    <div class="col-lg-9">
                        <input type="text" class="form-control" id="inputSurName" name="inputSurName"
                               placeholder="Surname">
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputDateFrom" class="col-lg-3 control-label">Birthday</label>

                    <label for="inputDateFrom" class="col-lg-1 control-label">from</label>

                    <div class="col-lg-3">
                        <input class="form-control" type="text" name="inputDateFrom" id="inputDateFrom"
                               placeholder="yyyy-MM-dd">
                    </div>

                    <label for="inputDateTo" class="col-lg-1 control-label">to</label>

                    <div class="col-lg-3">
                        <input class="form-control" type="text" name="inputDateTo" id="inputDateTo"
                               placeholder="yyyy-MM-dd">
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="inputSex" class="col-lg-3 control-label">Sex</label>

                        <div class="col-lg-9">
                            <select id="inputSex" name="inputSex" class="form-control">
                                <option>-None selected-</option>
                                <c:forEach var="sex" items="<%=Sex.values()%>">
                                    <option>${sex.displayName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="inputNationality" class="col-lg-3 control-label">Nationality</label>

                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="inputNationality" name="inputNationality"
                                   placeholder="Nationality">
                        </div>
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="relationshipStatus" class="col-lg-3 control-label">Relationship status</label>

                        <div class="col-lg-9">
                            <select id="relationshipStatus" nmae="relationshipStatus" class="form-control">
                                <c:forEach var="relationshipStatus" items="<%=RelationshipStatus.values()%>">
                                    <option>${relationshipStatus.displayName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="inputWebSite" class="col-lg-3 control-label">Web Site</label>

                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="inputWebSite" name="inputWebSite"
                                   placeholder="Web Site">
                        </div>
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="inputEmail" class="col-lg-3 control-label">Email</label>

                        <div class="col-lg-9">
                            <input type="email" class="form-control" id="inputEmail" name="inputEmail"
                                   placeholder="Email">
                        </div>
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="inputJob" class="col-lg-3 control-label">Job</label>

                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="inputJob" name="inputJob" placeholder="Job">
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputCountry" class="col-lg-3 control-label">Region</label>

                    <div class="col-lg-4">
                        <input type="text" class="form-control" id="inputCountry" name="inputCountry"
                               placeholder="Country">
                    </div>
                    <div class="col-lg-4">
                        <input type="text" class="form-control" id="inputCity" name="inputCity" placeholder="City">
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputStreet" class="col-lg-3 control-label">Address</label>

                    <div class="col-lg-5">
                        <input type="text" class="form-control" id="inputStreet" name="inputStreet"
                               placeholder="Street">
                    </div>
                    <div class="col-lg-2">
                        <input type="text" class="form-control" id="inputHouse" name="inputHouse" placeholder="House">
                    </div>
                    <div class="col-lg-2">
                        <input type="text" class="form-control" id="inputApartment" name="inputApartment"
                               placeholder="Apartment">
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputIndex" class="col-lg-3 control-label">Index</label>

                    <div class="col-lg-9">
                        <input type="text" class="form-control" id="inputIndex" name="inputIndex" placeholder="Index">
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" name="search" value="search">

        <div class="row">
            <div class="col-lg-6">
                <a class="btn btn-default btn-lg pull-right" href="http://localhost:8080/contacts">Cancel</a>
            </div>
            <div class="col-lg-6">
                <button type="submit" class="btn btn-info btn-lg pull-left" onclick="checkSearchForm();">Search</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>
