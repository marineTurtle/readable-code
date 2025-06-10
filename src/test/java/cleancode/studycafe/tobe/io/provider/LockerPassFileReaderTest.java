package cleancode.studycafe.tobe.io.provider;

import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LockerPassFileReaderTest {
  @DisplayName("사물함 이용권은 고정석 1종류만 존재한다.")
  @Test
  void lockerPassHasOnlyOneFixedPassType() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();
    List<StudyCafeSeatPass> hourly = seatPasses.findPassBy(StudyCafePassType.HOURLY);
    List<StudyCafeSeatPass> weekly = seatPasses.findPassBy(StudyCafePassType.WEEKLY);
    List<StudyCafeSeatPass> fixed = seatPasses.findPassBy(StudyCafePassType.FIXED);

    LockerPassFileReader lockerPassFileReader = new LockerPassFileReader();
    StudyCafeLockerPasses lockerPasses = lockerPassFileReader.getLockerPasses();

    // when
    Optional<StudyCafeLockerPass> lockerPass1 = lockerPasses.findLockerPassBy(hourly.get(0));
    Optional<StudyCafeLockerPass> lockerPass2 = lockerPasses.findLockerPassBy(weekly.get(0));
    Optional<StudyCafeLockerPass> lockerPass3 = lockerPasses.findLockerPassBy(fixed.get(0));

    // then
    assertThat(lockerPass1.isEmpty()).isTrue();
    assertThat(lockerPass2.isEmpty()).isTrue();
    assertThat(lockerPass3.isPresent()).isTrue();
  }
}
