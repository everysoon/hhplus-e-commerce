import http from 'k6/http';
import {check} from 'k6';

export let options = {
    vus: 50,           // 동시에 실행할 가상 사용자 수
    duration: '1m',    // 테스트 지속 시간 (1분)
};

export function setup() {

    const headers = {
        'Content-Type': 'application/json',
    };
    // 1. 테스트 상품 생성
    var productResponse = http.get("http://localhost:8224/api/test/products", {headers}); // product save
    check(productResponse, {
        'product created': (r) => r.status === 200 || r.status === 201,
    });

    // UUID 응답에서 추출
    console.log(`responseJson : ${JSON.stringify(productResponse.json())}`);
    const productIds = productResponse.json().data;

    if (!Array.isArray(productIds) || productIds.length === 0) {
        throw new Error("상품 생성 실패");
    }
    // 2. 유저 포인트 충전
    var price = 100000;
    var userIdResponse = http.post(`http://localhost:8224/api/test/charge-all?price=${price}`);
    console.log(`userIdResponse: ${JSON.stringify(userIdResponse.json().data)}`)
    var userIds = userIdResponse.json().data;
    if (!Array.isArray(userIds) || userIds.length === 0) {
        throw new Error("유저 리스트 비어있음");
    }
    return {productIds, userIds};
}

export default function (data) {
    const {userIds, productIds} = data;
    let userId = userIds[__VU % userIds.length]; // 가상 유저 수에 따라 분배
    let productId = productIds[__VU % productIds.length]; // 가상 유저 수에 따라 분배
    console.log(`userId : ${userId}`);
    console.log(`productId : ${productId}`);

    const payload = JSON.stringify({
        userId: userId, requestId: crypto.randomUUID(), products: [{
            productId: productId, quantity: 1
        }], couponId: []
    });
    console.log(`payload : ${payload}`);
    const headers = {'Content-Type': 'application/json'};
    const res = http.post(`http://localhost:8224/api/orders`, payload, {headers});

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
