package i15091.project.cmaker;

public enum Theme{
    ORANGE { public String getPath() {return new String("File:../resources/orange.css");  } }, 
    GREEN  { public String getPath() {return new String("File:../resources/green.css"); } },
    BLUE   { public String getPath() {return new String("File:../resources/blue.css"); } };

    abstract String getPath();

    public static String getCreatePath() {
      return new String(image_path + "create.png");
    }

    public static String getOpenPath() {
      return new String(image_path + "open.png");
    }
    
    public static String getLogoPath() {
      return new String(image_path + "logo.png");
    }

    public static String getArrowPath() {
      return new String(image_path + "back.png");
    }

    public static String getSettingsPath() {
      return new String(image_path + "settings.png");
    }

    public static String getSidebarClosePath() {
      return new String(image_path + "close_sidebar.png");
    }

    public static String getDonePath() {
      return new String(image_path + "done.png");
    }

    public static String getArrowRightPath() {
      return new String(image_path + "arrow_right.png");
    }

    public static String getDeletePath() {
      return new String(image_path + "delete.png");
    }

    public static String getAddPath() {
      return new String(image_path + "add.png");
    }

    public static String getAddCirclePath() {
      return new String(image_path + "add_circle.png");
    }

    private static final String image_path = new String("File:../resources/images/");
}