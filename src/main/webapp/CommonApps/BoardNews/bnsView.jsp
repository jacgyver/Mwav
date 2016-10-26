<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script src="//cdn.ckeditor.com/4.4.6/basic/ckeditor.js"></script>
<!-- imsi -->
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css"
	rel="stylesheet">


<!-- 소제목 -->
<!-- ----- 
색 : http://the3.tistory.com/49

<tr class="active">...</tr>
<tr class="success">...</tr>
<tr class="warning">...</tr>
<tr class="danger">...</tr>
<tr class="active">...</tr>

-->

<!-- Content Column -->
<div class="table-responsive">
	<form role="form">
		<table class="table table-striped">
			<thead>
				
				<tr class="active">
					<th>NO.</th>
					<th>Group</th>
					<th>ViewCount</th>
					<th>InsertDt</th>
					<th>staff_id</th>
					<th>Order</th>
				</tr>
			</thead>


			<tbody>
					<tr>
						<td>${selectOneBnsView.bNews_id}</td>
						<td>${selectOneBnsView.bnGroup}</td>
						<td>${selectOneBnsView.bnViewCount}</td>
						<td>${selectOneBnsView.bnInsertDt}</td>
						<td>${selectOneBnsView.staff_id}</td>
						<td>${selectOneBnsView.bnOrder}</td>
					</tr>
			</tbody>
		</table>

		<table class="table table-striped">

			<tr>
				<th class="active">Title</th>
			</tr>
			<tr> <td>${selectOneBnsView.bnTitle}</td></tr>
			



			<tr>
				<th class="active">SubTitle</th>
			</tr>
			<tr><td>${selectOneBnsView.bnSubTitle}</td></tr>

			<tr>
				<th class="active">Content</th>
				
			</tr>
			<tr><td>${selectOneBnsView.bnContent}</td></tr>

		</table>
	</form>

	<br style="clear: both">
	<p class="pull-right">
		<button type="button" class="btn btn-success" onClick="javascript:window.location.href='/board/bnsList.do'">All List</button>
		<button type="button" class="btn btn-warning" onClick="javascript:history.go(-1)">BACK</button>
	</p>

</div>
