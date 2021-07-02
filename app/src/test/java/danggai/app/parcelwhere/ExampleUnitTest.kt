package danggai.app.parcelwhere

import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.data.local.Tracks
import danggai.app.parcelwhere.util.CarrierUtil
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun carrierUtilTest() {
        val carrier = Mockito.mock(Carrier::class.java)

        Mockito.`when`(carrier.id).thenReturn("kr.cjlogistics")
        Mockito.`when`(carrier.idx).thenReturn(0)
        Mockito.`when`(carrier.name).thenReturn("CJ대한통운")
        Mockito.`when`(carrier.tel).thenReturn("+8215881255")

        assertTrue( CarrierUtil.getCarrierName(carrier.id) == carrier.name )
        assertTrue( CarrierUtil.getCarrierId(carrier.name) == carrier.id )
    }
}