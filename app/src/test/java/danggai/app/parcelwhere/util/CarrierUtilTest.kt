package danggai.app.parcelwhere.util

import danggai.app.parcelwhere.data.local.Carrier
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CarrierUtilTest {

    private val testMsg1 = """[Web발신]
    [CJ대한통운 택배_배송완료]
    
    고객님의 상품이 배송 완료되었습니다.
    
    ㆍ상품명 : 아기공룡둘리 인형 뉴둘리 25cm
    ㆍ인수자(위탁장소) : 테스트
    ㆍ운송장번호 : 6383_4930_9091
    
    ※ CJ대한통운 고객센터 : 1588-1255"""
    private val testMsg2 = """[떠리몰]
    ㅇㅇㅇ님 주문하신 상품의 배송안내입니다
    □주문번호 : 2104231817304483
    □송장번호 : 
    CJ대한통운/6397-4318-3730
    (단, 주문수량에 따라 추가 송장(박스)이 있을수 있습니다.)
    ▷[떠리몰] 바로가기 :
    www.thirtymall.com
    
    □기존 떠리몰 고객전화상담서비스가 종료됨에 따라, 이후 떠리몰 1:1게시판에 문의주시면 보다 정확하고 신속한 서비스로 고객님들에게 빠른 답변 드리도록 하겠습니다."""

    @Test
    fun testGetCarrierId() {
        Assert.assertTrue(CarrierUtil.getCarrierId(testMsg1) == "kr.cjlogistics")
        Assert.assertTrue(CarrierUtil.getCarrierId(testMsg2) == "kr.cjlogistics")
    }

    @Test
    fun testGetCarrierName() {
        Assert.assertTrue(CarrierUtil.getCarrierName("kr.cjlogistics") == "CJ대한통운")
        Assert.assertTrue(CarrierUtil.getCarrierName("kr.lotte") == "롯데택배")
        Assert.assertTrue(CarrierUtil.getCarrierName("kr.fakecarrier") == "")
    }

    @Test
    fun testCheckCarrierId() {
        Assert.assertTrue(CarrierUtil.checkCarrierId("kr.cjlogistics"))
        Assert.assertFalse(CarrierUtil.checkCarrierId("kr.fakecarrier"))
    }

    @Test
    fun carrierUtilTest() {
        val carrier = Mockito.mock(Carrier::class.java)

        Mockito.`when`(carrier.id).thenReturn("kr.cjlogistics")
//        Mockito.`when`(carrier.idx).thenReturn(0)
        Mockito.`when`(carrier.name).thenReturn("CJ대한통운")
//        Mockito.`when`(carrier.tel).thenReturn("+8215881255")

        Assert.assertTrue(CarrierUtil.getCarrierName(carrier.id) == carrier.name)
        Assert.assertTrue(CarrierUtil.getCarrierId(carrier.name) == carrier.id)
    }
}