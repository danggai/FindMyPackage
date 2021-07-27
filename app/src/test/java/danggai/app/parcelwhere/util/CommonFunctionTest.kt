package danggai.app.parcelwhere.util

import danggai.app.parcelwhere.Constant
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CommonFunctionTest {

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
    fun testGetTrackId() {
        Assert.assertTrue(CommonFunction.getTrackId(testMsg1) == "638349309091")
        Assert.assertTrue(CommonFunction.getTrackId(testMsg2) == "639743183730")
    }

    @Test
    fun testGetItemName() {
        Assert.assertTrue(CommonFunction.getItemName(testMsg1) == "아기공룡둘리 인형 뉴둘리 25cm")
        Assert.assertTrue(CommonFunction.getItemName(testMsg2) == Constant.DEFAULT_ITEM_NAME)
    }

    @Test
    fun testConvertDateString() {

    }
}