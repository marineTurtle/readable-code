package cleancode.studycafe.mission;

import cleancode.studycafe.mission.exception.AppException;
import cleancode.studycafe.mission.io.InputHandler;
import cleancode.studycafe.mission.io.OutputHandler;
import cleancode.studycafe.mission.io.StudyCafeFileHandler;
import cleancode.studycafe.mission.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

  public final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
  private final InputHandler inputHandler = new InputHandler();
  private final OutputHandler outputHandler = new OutputHandler();

  public void run() {
    try {
      outputHandler.showWelcomeMessage();
      outputHandler.showAnnouncement();
      outputHandler.askPassTypeSelection();

      StudyCafePassType passType = inputHandler.getPassTypeSelectingUserAction();

      List<StudyCafeSeatPass> seatPassList = getSeatPassList(passType);
      outputHandler.showPassListForSelection(seatPassList);

      List<StudyCafePass> studyCafePassList = new ArrayList<>();

      StudyCafeSeatPass selectedSeatPass = inputHandler.getSelectPass(seatPassList);
      studyCafePassList.add(selectedSeatPass);

      Optional<StudyCafeLockerPass> selectedLockerPass = getLockerPass(selectedSeatPass);
      selectedLockerPass.ifPresent(lockerPass -> {
        outputHandler.askLockerPass(lockerPass);
        boolean lockerSelection = inputHandler.getLockerSelection();
        if (lockerSelection) {
          studyCafePassList.add(lockerPass);
        }
      });

      StudyCafePasses studyCafePasses = StudyCafePasses.of(studyCafePassList);
      outputHandler.showPassOrderSummary(studyCafePasses);

    } catch (AppException e) {
      outputHandler.showSimpleMessage(e.getMessage());
    } catch (Exception e) {
      outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
    }
  }

  private List<StudyCafeSeatPass> getSeatPassList(StudyCafePassType studyCafePassType) {
    List<StudyCafeSeatPass> studyCafeSeatPasses = studyCafeFileHandler.readStudyCafePasses();
    return studyCafeSeatPasses.stream()
      .filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
      .toList();
  }

  private Optional<StudyCafeLockerPass> getLockerPass(StudyCafeSeatPass seatPass) {
    if (seatPass.isLockerAvailable()) {
      List<StudyCafeLockerPass> lockerPasses = studyCafeFileHandler.readLockerPasses();
      return lockerPasses.stream()
        .filter(lockerPass -> lockerPass.isSamePassType(seatPass))
        .findFirst();
    }

    return Optional.empty();
  }
}
