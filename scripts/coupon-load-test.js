import http from 'k6/http';
import {check} from 'k6';

export let options = {
    vus: 100,           // 동시에 실행할 가상 사용자 수
    duration: '30s',    // 테스트 지속 시간
};
let userIds = Array.from({length: 150}, (_, i) => i + 1);

export function setup() {

    const headers = {
        'Content-Type': 'application/json',
    };

    var payload = JSON.stringify({
        couponType: "FIXED",
        description: "TestCoupon",
        remainingStock: 50000,
        discountAmount: 10000,
        expiredAt: new Date().toISOString()
    });
    var res = http.post("http://localhost:8224/api/coupons", payload, {headers}); // coupon save


    check(res, {
        'coupon created': (r) => r.status === 200 || r.status === 201,
    });

    // UUID 응답에서 추출
    const responseJson = res.json();
    console.log(`responseJson : ${JSON.stringify(responseJson)}`);
    const couponId = responseJson.data?.couponId;

    if (!couponId) {
        throw new Error("쿠폰 생성 실패 또는 couponId 없음");
    }

    return {couponId};
}

export default function (data) {
    const { couponId } = data;
    let userId = userIds[__VU % userIds.length]; // 가상 유저 수에 따라 분배
    console.log(`userId : ${userId}`);

    const res = http.post(`http://localhost:8224/api/coupons/${userId}?couponId=${couponId}`);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
