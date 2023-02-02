<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- jQuery -->
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
    <!-- iamport.payment.js -->
    <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
    <script>
        var IMP = window.IMP; 
        IMP.init("imp87432616"); 
    
        //function requestPay() {
            IMP.request_pay({
                pg : 'html5_inicis.INIpayTest',
                pay_method : 'card',
                // merchant_uid: "57008833-33004", 
                merchant_uid: "javawspring_" + new Date().getTime(), 
                // name : '당근 10kg',
                name : '${vo.name}',
                // amount : 1004,
                amount : '${vo.amount}',
                // buyer_email : 'Iamport@chai.finance',
                buyer_email : '${vo.buyer_email}',
                // buyer_name : '아임포트 기술지원팀',
                buyer_name : '${vo.buyer_name}',
                // buyer_tel : '010-1234-5678',
                buyer_tel : '${vo.buyer_tel}',
                // buyer_addr : '서울특별시 강남구 삼성동',
                buyer_addr : '${vo.buyer_addr}',
                // buyer_postcode : '123-456'
                buyer_postcode : '${vo.buyer_postcode}',
            }, function (rsp) { // callback
                if (rsp.success) {
                	  alert("결재가 완료되었습니다.");
                    console.log(rsp);
                    location.href = '${ctp}/study/merchantOk';
                } else {
                	  alert("결재가 취소 되었습니다.");
                    console.log(rsp);
                    location.href = '${ctp}/study/merchant';
                }
            });
        //}
    </script>
    <meta charset="UTF-8">
    <title>Sample Payment</title>
</head>
<body>
    <!-- <button onclick="requestPay()">결제하기</button> --> <!-- 결제하기 버튼 생성 -->
    <p>
      <img src="${ctp}/images/payment.gif" />
    </p>
</body>
</html>