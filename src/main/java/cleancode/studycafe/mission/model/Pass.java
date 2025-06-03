package cleancode.studycafe.mission.model;

public interface Pass {
  StudyCafePassType getPassType();

  int getDuration();

  int getPrice();

  String display();
}
