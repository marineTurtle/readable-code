package cleancode.studycafe.mission.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudyCafePasses {
  private final List<StudyCafePass> studyCafePasses;

  private StudyCafePasses(List<StudyCafePass> studyCafePasses) {
    this.studyCafePasses = studyCafePasses;
  }

  public static StudyCafePasses of(List<StudyCafePass> studyCafePasses) {
    return new StudyCafePasses(studyCafePasses);
  }

  // 판별
  public boolean hasDiscountPrice() {
    return getDiscountPrice() > 0;
  }

  // 조회
  public int getDiscountPrice() {
    Optional<StudyCafeSeatPass> seatPass = getSeatPass();
    return seatPass.map(StudyCafeSeatPass::getDiscountPrice).orElse(0);
  }

  public int getTotalPrice() {
    Optional<StudyCafeSeatPass> seatPass = getSeatPass();
    Optional<StudyCafeLockerPass> lockerPass = getLockerPass();

    int seatPrice = seatPass.map(StudyCafeSeatPass::getTotalPrice).orElse(0);
    int lockerPrice = lockerPass.map(StudyCafeLockerPass::getPrice).orElse(0);

    return seatPrice + lockerPrice;
  }
  
  public Optional<StudyCafeSeatPass> getSeatPass() {
    ArrayList<StudyCafePass> passes = new ArrayList<>(studyCafePasses);
    return passes.stream()
      .filter(StudyCafeSeatPass.class::isInstance)
      .map(StudyCafeSeatPass.class::cast)
      .findFirst();
  }

  public Optional<StudyCafeLockerPass> getLockerPass() {
    ArrayList<StudyCafePass> passes = new ArrayList<>(studyCafePasses);
    return passes.stream()
      .filter(StudyCafeLockerPass.class::isInstance)
      .map(StudyCafeLockerPass.class::cast)
      .findFirst();
  }
}
