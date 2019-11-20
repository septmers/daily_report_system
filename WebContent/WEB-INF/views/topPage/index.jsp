<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id= "flush_success">
                <c:out value = "${flush}"/>
            </div>
        </c:if>
        <h2>日報管理システムへようこそ</h2>
        <h3>【自分の日報一覧】</h3>
        <table id = "report_list">
            <tbody>
                <tr>
                    <th class = "report_name">氏名</th>
                    <th class = "report_date">日付</th>
                    <th class = "report_title">タイトル</th>
                    <th class = "report_action">操作</th>
                </tr>
                <c:forEach var = "report" items = "${reports}" varStatus = "status">
                    <tr class = "row${status.count %2}">
                        <td><c:out value = "${report.employee.name}" /></td>
                        <td><fmt:formatDate value = "${report.report_date}" pattern = "yyyy-MM-dd" /></td>
                        <td><c:out value = "${report.title}" /></td>
                        <td><a href = "<c:url value = '/reports/show?id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id = "pagination">
            （全 ${reports_count} 件）<br />
            <div id = "page_container">
                <c:forEach var ="i" begin = "1" end = "${((reports_count - 1) / 15) +1 }" step = "1">
                    <c:choose>
                        <c:when test = "${i == page}">
                            <div class = "page_item"><c:out value = "${i}" />&nbsp;</div>
                        </c:when>
                        <c:otherwise>
                            <div class = "page_item"><a href = "<c:url value = '/?page=${i}' />"><c:out value = "${i}" /></a>&nbsp;</div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
        <p><a href= "<c:url value = '/reports/new' />">新規日報の登録</a></p>
    </c:param>
</c:import>