package game;

@SuppressWarnings("ConstantConditions")
public class Graphics {
    //creates a new global variable to determine the characters to be drawn
    public static GlobalVar GFX_STYLE = new GlobalVar("gfxStyle", 1, 2,
            "Simple Text", "Pipe Frames", "Solid Frames");

    //entities
    public static final char NULL_CHAR = 'N';
    public static final char NEW_LINE = '\n';
    public static final char PLAYER_CHAR = 'X';
    public static final char BLANK_CHAR = ' ';

    //FRAME SYMBOLS - possibly redundant, but could also be used outside of drawing frames
    //pipe based framing
    public static final char PIPE_TOP_LEFT = '\u2554'; // ╔
    public static final char PIPE_TOP_RIGHT = '\u2557'; // ╗
    public static final char PIPE_BOT_LEFT = '\u255a'; // ╚
    public static final char PIPE_BOT_RIGHT = '\u255d'; // ╝
    public static final char PIPE_VERTICAL = '\u2551'; // ║
    public static final char PIPE_HORIZONTAL = '\u2550'; // ═
    public static final char PIPE_V_RIGHT = '\u2560'; // ╠
    public static final char PIPE_V_LEFT = '\u2563'; // ╣
    public static final char PIPE_H_DOWN = '\u2566'; // ╦
    public static final char PIPE_H_TOP = '\u2569'; // ╩

    //solid characters framing - buggy in windows' cmd
    public static final char SOLID_FULL_BLOCK = '\u2588'; // █
    public static final char SOLID_DARK_SHADE = '\u2593'; // ▓
    public static final char SOLID_MEDIUM_SHADE = '\u2592'; // ▒
    public static final char SOLID_LIGHT_SHADE = '\u2591'; // ░
    public static final char SOLID_TOP_LEFT = '\u259B'; // ▛
    public static final char SOLID_TOP_RIGHT = '\u259C'; // ▜
    public static final char SOLID_BOT_LEFT = '\u2599'; // ▙
    public static final char SOLID_BOT_RIGHT = '\u259F'; // ▟
    public static final char SOLID_UPPER_HALF = '\u2580'; // ▀
    public static final char SOLID_LOWER_HALF = '\u2584'; // ▄
    public static final char SOLID_LEFT_HALF = '\u258C'; // ▌
    public static final char SOLID_RIGHT_HALF = '\u2590'; // ▐

    //simple text based framing
    public static final char SIMPLE_CROSS = '+'; // +
    public static final char SIMPLE_VERTICAL = '|'; // |
    public static final char SIMPLE_HORIZONTAL = '-'; // -
//END FRAME SYMBOLS

    //world entities
    public static final Character[] ENTITIES =
            {
                    NULL_CHAR,
                    '.',
                    '#',
                    PLAYER_CHAR
            };
    //extended version
    public static final Character[] XENTITIES =
            {
                    NULL_CHAR,
                    SOLID_LIGHT_SHADE,
                    SOLID_DARK_SHADE,
                    PLAYER_CHAR
            };

    //all drawables
    public static char FRAME_VERTICAL;
    public static char FRAME_HORIZONTAL;
    public static char FRAME_TOP_LEFT;
    public static char FRAME_TOP_RIGHT;
    public static char FRAME_V_LEFT;
    public static char FRAME_BOT_RIGHT;
    public static char FRAME_BOT_LEFT;
    public static char FRAME_V_RIGHT;
    public static char FRAME_TOP;
    public static char FRAME_BOT;
    public static char FRAME_LEFT;
    public static char FRAME_RIGHT;
    public static char FRAME_H_DOWN;
    public static char FRAME_H_TOP;
    public static char ENTITY_FLOOR;
    public static char ENTITY_WALL;

    //set the drawables
    static {
        refresh();
    }

    //refresh the values of the drawables
    public static void refresh() {
        //Entity drawing
        switch (GFX_STYLE.getIntValue()) {
            case 1:
            case 2:
                ENTITY_FLOOR = XENTITIES[1];
                ENTITY_WALL = XENTITIES[2];
                break;
            default:
                ENTITY_FLOOR = ENTITIES[1];
                ENTITY_WALL = ENTITIES[2];
                break;
        }

        //Framing
        switch (GFX_STYLE.getIntValue()) {
            //simple text framing
            case 0:
                FRAME_TOP_LEFT = SIMPLE_CROSS;
                FRAME_TOP_RIGHT = SIMPLE_CROSS;
                FRAME_BOT_LEFT = SIMPLE_CROSS;
                FRAME_BOT_RIGHT = SIMPLE_CROSS;
                FRAME_VERTICAL = SIMPLE_VERTICAL;
                FRAME_HORIZONTAL = SIMPLE_HORIZONTAL;
                FRAME_TOP = SIMPLE_HORIZONTAL;
                FRAME_BOT = SIMPLE_HORIZONTAL;
                FRAME_LEFT = SIMPLE_VERTICAL;
                FRAME_RIGHT = SIMPLE_VERTICAL;
                FRAME_V_RIGHT = SIMPLE_CROSS;
                FRAME_V_LEFT = SIMPLE_CROSS;
                break;

            //pipe framing
            case 1:
                FRAME_TOP_LEFT = PIPE_TOP_LEFT;
                FRAME_TOP_RIGHT = PIPE_TOP_RIGHT;
                FRAME_BOT_LEFT = PIPE_BOT_LEFT;
                FRAME_BOT_RIGHT = PIPE_BOT_RIGHT;
                FRAME_TOP = PIPE_HORIZONTAL;
                FRAME_BOT = PIPE_HORIZONTAL;
                FRAME_HORIZONTAL = PIPE_HORIZONTAL;
                FRAME_LEFT = PIPE_VERTICAL;
                FRAME_RIGHT = PIPE_VERTICAL;
                FRAME_VERTICAL = PIPE_VERTICAL;
                FRAME_V_RIGHT = PIPE_V_RIGHT;
                FRAME_V_LEFT = PIPE_V_LEFT;
                FRAME_H_DOWN = PIPE_H_DOWN;
                FRAME_H_TOP = PIPE_H_TOP;
                break;

            //solid framing
            case 2:
                FRAME_TOP_LEFT = SOLID_TOP_LEFT;
                FRAME_TOP_RIGHT = SOLID_TOP_RIGHT;
                FRAME_BOT_LEFT = SOLID_BOT_LEFT;
                FRAME_BOT_RIGHT = SOLID_BOT_RIGHT;
                FRAME_TOP = SOLID_UPPER_HALF;
                FRAME_BOT = SOLID_LOWER_HALF;
                FRAME_HORIZONTAL = SOLID_UPPER_HALF;
                FRAME_LEFT = SOLID_LEFT_HALF;
                FRAME_RIGHT = SOLID_RIGHT_HALF;
                FRAME_VERTICAL = SOLID_FULL_BLOCK;
                FRAME_V_RIGHT = SOLID_TOP_LEFT;
                FRAME_V_LEFT = SOLID_TOP_RIGHT;
                FRAME_H_DOWN = SOLID_FULL_BLOCK;
                FRAME_H_TOP = SOLID_FULL_BLOCK;
                break;
        }
    }

    public static char[] getFrameList() {
        return new char[]{FRAME_VERTICAL, FRAME_HORIZONTAL, FRAME_TOP_LEFT, FRAME_TOP_RIGHT, FRAME_V_LEFT, FRAME_BOT_RIGHT,
                FRAME_BOT_LEFT, FRAME_V_RIGHT, FRAME_TOP, FRAME_BOT, FRAME_LEFT, FRAME_RIGHT, FRAME_H_DOWN, FRAME_H_TOP};
    }

    //get a 2d box
    public static Surface box(char symbol, int width, int height) {
        Surface line = new Surface(width, height);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                line.setChar(j, i, symbol);
        return line;
    }

    //horizontal line for the menu
    //this method does not return a Surface, thus it doesn't require a height attribute
    public static String hMenuLine(char symbol, int length) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < length; i++)
            line.append(symbol);

        return line.toString();
    }

    //get a horizontal or vertical line
    public static Surface hLine(char symbol, int length) {
        return box(symbol, length, 1);
    }

    public static Surface vLine(char symbol, int length) {
        return box(symbol, 1, length);
    }

    //centers a text in a given length
    //taken from stackoverflow
    public static String centerString(String text, int len) {
        return centerString(text, len, ' ');
    }

    //same as above, but with a custom character as padding
    public static String centerString(String text, int len, char symbol) {
        String format = "%" + len + "s%s%" + len + "s";
        String out = String.format(format, "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.replace(' ', symbol).substring((int) start, (int) end);
    }
}
