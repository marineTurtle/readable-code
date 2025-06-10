package cleancode.studycafe.tobe.model.pass;

import cleancode.studycafe.tobe.io.provider.SeatPassFileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudyCafeSeatPassTest {

  @DisplayName("좌석 이용권이 고정석인 경우 사물함을 사용할 수 있다.")
  @Test
  void fixedSeatPassCanUseLocker() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();

    // when
    List<StudyCafeSeatPass> weekly = seatPasses.findPassBy(StudyCafePassType.WEEKLY);
    List<StudyCafeSeatPass> fixed = seatPasses.findPassBy(StudyCafePassType.FIXED);

    // then
    assertThat(weekly.get(0).cannotUseLocker()).isTrue();
    assertThat(fixed.get(0).cannotUseLocker()).isFalse();
  }
}
