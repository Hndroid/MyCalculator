package com.anull.mycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private Button[] btn = new Button[12];// 0~9 + E + pi 十二个数字
    private TextView tvShow;// 用于显示输出结果
    private Button div, mul, sub, add, equal, sin, cos, tan, log, ln, sqrt,
            square, factorial, back, left, right, point, clear;
    public String str_old;
    public String str_new;
    public boolean vbegin = true;// 控制输入，true为重新输入，false为接着输入
    public double pi = 4 * Math.atan(1);// π值
    public boolean tip_lock = true;// true为正确，可以继续输入，false错误，输入锁定
    public boolean equals_flag = true;// 是否在按下=之后输入，true为之前，false为之后

    private List<Button> btnList;
    private TextView prefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //初始化按钮
    public void initView() {
        tvShow = (TextView) findViewById(R.id.inputOrOutput);
        prefix = (TextView) findViewById(R.id.prefix);
        div = (Button) findViewById(R.id.division);
        mul = (Button) findViewById(R.id.mutiply);
        sub = (Button) findViewById(R.id.Subtraction);
        add = (Button) findViewById(R.id.add);
        equal = (Button) findViewById(R.id.equal);
        sin = (Button) findViewById(R.id.sin);
        cos = (Button) findViewById(R.id.cos);
        tan = (Button) findViewById(R.id.tan);
        log = (Button) findViewById(R.id.lg);
        ln = (Button) findViewById(R.id.ln);
        sqrt = (Button) findViewById(R.id.sqrt);
        square = (Button) findViewById(R.id.square);
        factorial = (Button) findViewById(R.id.factorial);
        back = (Button) findViewById(R.id.back);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        point = (Button) findViewById(R.id.point);
        clear = (Button) findViewById(R.id.clear);
        btn[0] = (Button) findViewById(R.id.zero);
        btn[1] = (Button) findViewById(R.id.one);
        btn[2] = (Button) findViewById(R.id.two);
        btn[3] = (Button) findViewById(R.id.three);
        btn[4] = (Button) findViewById(R.id.four);
        btn[5] = (Button) findViewById(R.id.five);
        btn[6] = (Button) findViewById(R.id.six);
        btn[7] = (Button) findViewById(R.id.seven);
        btn[8] = (Button) findViewById(R.id.eight);
        btn[9] = (Button) findViewById(R.id.nine);
        btn[10] = (Button) findViewById(R.id.e);
        btn[11] = (Button) findViewById(R.id.pi);

        btnList = new ArrayList<>();

        for (int i = 0; i < btn.length; i++) {
            btnList.add(btn[i]);
        }
        btnList.add(div);
        btnList.add(mul);
        btnList.add(sub);
        btnList.add(add);
        btnList.add(equal);
        btnList.add(sin);
        btnList.add(cos);
        btnList.add(tan);
        btnList.add(log);
        btnList.add(ln);
        btnList.add(sqrt);
        btnList.add(square);
        btnList.add(factorial);
        btnList.add(back);
        btnList.add(left);
        btnList.add(right);
        btnList.add(point);
        btnList.add(clear);

        for (int i = 0; i < btnList.size(); i++) {
            //给每一个按钮绑定监听事件
            setListenerForEachButton(btnList.get(i));
        }
    }

    //给每一个按钮绑定监听事件
    private void setListenerForEachButton(final Button btn) {
        btn.setOnClickListener(actionPerformed);
    }

    /**
     * 键盘命令捕捉
     */
    String[] TipCommand = new String[500];
    int tip_i = 0;// TipCommand的指针
    private View.OnClickListener actionPerformed = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // 按键上的命令获取
            String command = ((Button) view).getText().toString();
            // 获取显示器上的字符串
            String str = tvShow.getText().toString();
            // 检测输入是否合法
            if (equals_flag == false //在按 = 之后输入，不是的话，直接跳过
                    && "0123456789Eπ.()sincostanlnlogn!+-×÷√^".indexOf(command) != -1) {
                // 检测显示器上的字符串是否合法
                if (checkStr(str)) {  //——>检测显示器上的字符是否合法，true为合法
                    if ("+-×÷√^)".indexOf(command) != -1) {
                        for (int i = 0; i < str.length(); i++) {
                            TipCommand[tip_i] = String.valueOf(str.charAt(i));
                            tip_i++;  // TipCommand的指针
                        }
                        vbegin = false;//控制输入 ——>false为接着输入
                    }
                } else {
                    tvShow.setText("0");
                    prefix.setText("");
                    vbegin = true;
                    tip_i = 0;
                    tip_lock = true;
                }
                equals_flag = true;
            }
            if (tip_i > 0) // TipCommand的指针
                TipChecker(TipCommand[tip_i - 1], command);
            else if (tip_i == 0) {
                TipChecker("#", command);//当第一次输入按键的时候，检查是否输入合法
            }
            if ("0123456789Eπ.()sincostanlnlogn!+-×÷√^".indexOf(command) != -1
                    && tip_lock) {//tip_lock 可以持续输入
                TipCommand[tip_i] = command;
                tip_i++;
            }
            // 如果输入正确，就将输入信息显示到显示器上
            if ("0123456789Eπ.()sincostanlnlogn!+-×÷√^".indexOf(command) != -1
                    && tip_lock) { // 共25个按键
                show(command);
                // 如果输入时退格键，并且是在按=之前
            } else if (command.compareTo("←") == 0 && equals_flag) {
                // 一次删除3个字符
                if (delChar(str) == 3) {
                    if (str.length() > 3)
                        tvShow.setText(str.substring(0, str.length() - 3));
                    else if (str.length() == 3) {
                        tvShow.setText("0");
                        prefix.setText("");
                        vbegin = true;
                        tip_i = 0;
                    }
                    // 依次删除2个字符
                } else if (delChar(str) == 2) {
                    if (str.length() > 2)
                        tvShow.setText(str.substring(0, str.length() - 2));
                    else if (str.length() == 2) {
                        tvShow.setText("0");
                        prefix.setText("");
                        vbegin = true;
                        tip_i = 0;
                    }
                    // 依次删除一个字符
                } else if (delChar(str) == 1) {
                    // 若之前输入的字符串合法则删除一个字符
                    if (checkStr(str)) {
                        if (str.length() > 1)
                            tvShow.setText(str.substring(0, str.length() - 1));
                        else if (str.length() == 1) {
                            tvShow.setText("0");
                            prefix.setText("");
                            vbegin = true;
                            tip_i = 0;
                        }
                        // 若之前输入的字符串不合法则删除全部字符
                    } else {
                        tvShow.setText("0");
                        prefix.setText("");
                        vbegin = true;
                        tip_i = 0;
                    }
                }
                if (tvShow.getText().toString().compareTo("-") == 0
                        || equals_flag == false) {
                    tvShow.setText("0");
                    prefix.setText("");
                    vbegin = true;
                    tip_i = 0;
                }
                tip_lock = true;
                if (tip_i > 0)
                    tip_i--;
                // 如果是在按=之后输入退格键
            } else if (command.compareTo("←") == 0 && equals_flag == false) {
                // 将显示器内容设置为0
                tvShow.setText("0");
                prefix.setText("");
                vbegin = true;
                tip_i = 0;
                tip_lock = true;
                // 如果输入的是清除键
            } else if (command.compareTo("C") == 0) {
                // 将显示器内容设置为0
                tvShow.setText("0");
                prefix.setText("");
                // 重新输入标志置为true
                vbegin = true;
                // 缓存命令位数清0
                tip_i = 0;
                // 表明可以继续输入
                tip_lock = true;
                // 表明输入=之前
                equals_flag = true;
                // 如果输入的是=号，并且输入合法
            } else if (command.compareTo("=") == 0 && tip_lock && checkStr(str)
                    && equals_flag) {
                tip_i = 0; // TipCommand的指针
                // 表明不可以继续输入
                tip_lock = false;
                // 表明输入=之后
                equals_flag = false;
                // 保存原来算式样子
                str_old = str;
                // 替换算式中的运算符，便于计算
                str = str.replaceAll("sin", "s");
                str = str.replaceAll("cos", "c");
                str = str.replaceAll("tan", "t");
                str = str.replaceAll("log", "g");
                str = str.replaceAll("ln", "l");
                str = str.replaceAll("n!", "!");
                // 重新输入标志设置true
                vbegin = true;
                // 将-1x转换成-
                str_new = str.replaceAll("-", "-1×");
                // 计算算式结果
                new calc().process(str_new);
            }
            // 表明可以继续输入
            tip_lock = true;
        }
    };

    /*
     * 检测函数，对str进行前后语法检测 为Tip的提示方式提供依据，与TipShow()配合使用 编号 字符 其后可以跟随的合法字符 1 （
     * 数字|（|-|.|函数 2 ） 算符|）|√ ^ 3 . 数字|算符|）|√ ^ 4 数字 .|数字|算符|）|√ ^ 5 算符
     * 数字|（|.|函数 6 √ ^ （ |. | 数字 7 函数 数字|（|.
     *
     * 小数点前后均可省略，表示0 数字第一位可以为0
     */
    private void TipChecker(String tipcommand1, String tipcommand2) {

        // Tipcode1表示错误类型，Tipcode2表示名词解释类型
        int Tipcode1 = 0, Tipcode2 = 0;
        // 表示命令类型
        int tiptype1 = 0, tiptype2 = 0;
        // 括号数
        int bracket = 0;
        // “+-x÷√^”不能作为第一位
        //这里是第一次在键盘上面按钮的时候进入的逻辑
        if (tipcommand1.compareTo("#") == 0
                && (tipcommand2.compareTo("÷") == 0
                || tipcommand2.compareTo("×") == 0
                || tipcommand2.compareTo("+") == 0
                || tipcommand2.compareTo(")") == 0
                || tipcommand2.compareTo("√") == 0 || tipcommand2
                .compareTo("^") == 0)) {
            Tipcode1 = -1;
        }
        // 定义存储字符串中最后一位的类型
        //第一次输入之后进入的逻辑界面
        else if (tipcommand1.compareTo("#") != 0) {
            if (tipcommand1.compareTo("(") == 0) {
                tiptype1 = 1;
            } else if (tipcommand1.compareTo(")") == 0) {
                tiptype1 = 2;
            } else if (tipcommand1.compareTo(".") == 0) {
                tiptype1 = 3;
            } else if ("0123456789Eπ".indexOf(tipcommand1) != -1) {
                tiptype1 = 4;
            } else if ("+-×÷".indexOf(tipcommand1) != -1) {
                tiptype1 = 5;
            } else if ("√^".indexOf(tipcommand1) != -1) {
                tiptype1 = 6;
            } else if ("sincostanloglnn!".indexOf(tipcommand1) != -1) {
                tiptype1 = 7;
            }
            // 定义欲输入的按键类型
            if (tipcommand2.compareTo("(") == 0) {
                tiptype2 = 1;
            } else if (tipcommand2.compareTo(")") == 0) {
                tiptype2 = 2;
            } else if (tipcommand2.compareTo(".") == 0) {
                tiptype2 = 3;
            } else if ("0123456789Eπ".indexOf(tipcommand2) != -1) {
                tiptype2 = 4;
            } else if ("+-×÷".indexOf(tipcommand2) != -1) {
                tiptype2 = 5;
            } else if ("√^".indexOf(tipcommand2) != -1) {
                tiptype2 = 6;
            } else if ("sincostanloglnn!".indexOf(tipcommand2) != -1) {
                tiptype2 = 7;
            }

            switch (tiptype1) {
                case 1:
                    // 左括号后面直接接右括号,“+x÷”（负号“-”不算）,或者"√^"
                    if (tiptype2 == 2
                            || (tiptype2 == 5 && tipcommand2.compareTo("-") != 0)
                            || tiptype2 == 6)
                        Tipcode1 = 1;
                    break;
                case 2:
                    // 右括号后面接左括号，数字，“+-x÷sin^...”
                    if (tiptype2 == 1 || tiptype2 == 3 || tiptype2 == 4
                            || tiptype2 == 7)
                        Tipcode1 = 2;
                    break;
                case 3:
                    // “.”后面接左括号或者“sincos...”
                    if (tiptype2 == 1 || tiptype2 == 7)
                        Tipcode1 = 3;
                    // 连续输入两个“.”
                    if (tiptype2 == 3)
                        Tipcode1 = 8;
                    break;
                case 4:
                    // 数字后面直接接左括号或者“sincos...”
                    if (tiptype2 == 1 || tiptype2 == 7)
                        Tipcode1 = 4;
                    break;
                case 5:
                    // “+-x÷”后面直接接右括号，“+-x÷√^”
                    if (tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6)
                        Tipcode1 = 5;
                    break;
                case 6:
                    // “√^”后面直接接右括号，“+-x÷√^”以及“sincos...”
                    if (tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6
                            || tiptype2 == 7)
                        Tipcode1 = 6;
                    break;
                case 7:
                    // “sincos...”后面直接接右括号“+-x÷√^”以及“sincos...”
                    if (tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6
                            || tiptype2 == 7)
                        Tipcode1 = 7;
                    break;
            }
        }
        // 检测小数点的重复性，Tipconde1=0,表明满足前面的规则
        if (Tipcode1 == 0 && tipcommand2.compareTo(".") == 0) {
            int tip_point = 0;
            for (int i = 0; i < tip_i; i++) {
                // 若之前出现一个小数点点，则小数点计数加1
                if (TipCommand[i].compareTo(".") == 0) {
                    tip_point++;
                }
                // 若出现以下几个运算符之一，小数点计数清零
                if (TipCommand[i].compareTo("sin") == 0
                        || TipCommand[i].compareTo("cos") == 0
                        || TipCommand[i].compareTo("tan") == 0
                        || TipCommand[i].compareTo("log") == 0
                        || TipCommand[i].compareTo("ln") == 0
                        || TipCommand[i].compareTo("n!") == 0
                        || TipCommand[i].compareTo("√") == 0
                        || TipCommand[i].compareTo("^") == 0
                        || TipCommand[i].compareTo("÷") == 0
                        || TipCommand[i].compareTo("×") == 0
                        || TipCommand[i].compareTo("-") == 0
                        || TipCommand[i].compareTo("+") == 0
                        || TipCommand[i].compareTo("(") == 0
                        || TipCommand[i].compareTo(")") == 0) {
                    tip_point = 0;
                }
            }
            tip_point++;
            // 若小数点计数大于1，表明小数点重复了
            if (tip_point > 1) {
                Tipcode1 = 8;
            }
        }
        // 检测右括号是否匹配
        if (Tipcode1 == 0 && tipcommand2.compareTo(")") == 0) {
            int tip_right_bracket = 0;
            for (int i = 0; i < tip_i; i++) {
                // 如果出现一个左括号，则计数加1
                if (TipCommand[i].compareTo("(") == 0) {
                    tip_right_bracket++;
                }
                // 如果出现一个右括号，则计数减1
                if (TipCommand[i].compareTo(")") == 0) {
                    tip_right_bracket--;
                }
            }
            // 如果右括号计数=0,表明没有响应的左括号与当前右括号匹配
            if (tip_right_bracket == 0) {
                Tipcode1 = 10;
            }
        }
        // 检查输入=的合法性
        if (Tipcode1 == 0 && tipcommand2.compareTo("=") == 0) {
            // 括号匹配数
            int tip_bracket = 0;
            for (int i = 0; i < tip_i; i++) {
                if (TipCommand[i].compareTo("(") == 0) {
                    tip_bracket++;
                }
                if (TipCommand[i].compareTo(")") == 0) {
                    tip_bracket--;
                }
            }
            // 若大于0，表明左括号还有未匹配的
            if (tip_bracket > 0) {
                Tipcode1 = 9;
                bracket = tip_bracket;
            } else if (tip_bracket == 0) {
                // 若前一个字符是以下之一，表明=号不合法
                if ("√^sincostanloglnn!".indexOf(tipcommand1) != -1) {
                    Tipcode1 = 6;
                }
                // 若前一个字符是以下之一，表明=号不合法
                if ("+-×÷".indexOf(tipcommand1) != -1) {
                    Tipcode1 = 5;
                }
            }
        }
        // 若命令式以下之一，则显示相应的帮助信息
        if (tipcommand2.compareTo("MC") == 0)
            Tipcode2 = 1;
        if (tipcommand2.compareTo("C") == 0)
            Tipcode2 = 2;
        if (tipcommand2.compareTo("DRG") == 0)
            Tipcode2 = 3;
        if (tipcommand2.compareTo("←") == 0)
            Tipcode2 = 4;
        if (tipcommand2.compareTo("sin") == 0)
            Tipcode2 = 5;
        if (tipcommand2.compareTo("cos") == 0)
            Tipcode2 = 6;
        if (tipcommand2.compareTo("tan") == 0)
            Tipcode2 = 7;
        if (tipcommand2.compareTo("log") == 0)
            Tipcode2 = 8;
        if (tipcommand2.compareTo("ln") == 0)
            Tipcode2 = 9;
        if (tipcommand2.compareTo("n!") == 0)
            Tipcode2 = 10;
        if (tipcommand2.compareTo("√") == 0)
            Tipcode2 = 11;
        if (tipcommand2.compareTo("^") == 0)
            Tipcode2 = 12;
        // 显示帮助和错误信息
        TipShow(bracket, Tipcode1, Tipcode2, tipcommand1, tipcommand2);
    }

    /*
     * 反馈Tip信息，加强人机交互，与TipChecker()配合使用
     */
    private void TipShow(int bracket, int tipcode1, int tipcode2,
                         String tipcommand1, String tipcommand2) {

        String tipmessage = "";
        if (tipcode1 != 0)
            tip_lock = false;// 表明输入有误
    }

    /**
     * 将信息显示在显示屏上
     */
    private void show(String str) {
        // 清屏后输出
        if (vbegin) {
            tvShow.setText(str);
        } else {
            tvShow.append(str);
        }
        vbegin = false;
    }

    /*
     * 检测函数，返回值为3、2、1 表示应当一次删除几个？ Three+Two+One = TTO 为←按钮的删除方式提供依据
     * 返回3，表示str尾部为sin、cos、tan、log中的一个，应当一次删除3个 返回2，表示str尾部为ln、n!中的一个，应当一次删除2个
     * 返回1，表示为除返回3、2外的所有情况，只需删除一个（包含非法字符时要另外考虑：应清屏）
     */
    private int delChar(String str) {
        if ((str.charAt(str.length() - 1) == 'n'
                && str.charAt(str.length() - 2) == 'i' && str.charAt(str
                .length() - 3) == 's')
                || (str.charAt(str.length() - 1) == 's'
                && str.charAt(str.length() - 2) == 'o' && str
                .charAt(str.length() - 3) == 'c')
                || (str.charAt(str.length() - 1) == 'n'
                && str.charAt(str.length() - 2) == 'a' && str
                .charAt(str.length() - 3) == 't')
                || (str.charAt(str.length() - 1) == 'g'
                && str.charAt(str.length() - 2) == 'o' && str
                .charAt(str.length() - 3) == 'l')) {
            return 3;
        } else if ((str.charAt(str.length() - 1) == 'n' && str.charAt(str
                .length() - 2) == 'l')
                || (str.charAt(str.length() - 1) == '!' && str.charAt(str
                .length() - 2) == 'n')) {
            return 2;
        } else {
            return 1;
        }
    }

    /*
     * 判断一个str是否是合法的，返回值为true、false
     * 只包含0123456789Eπ.()sincostanlnlogn!+-×÷√^的是合法的str，返回true
     * 包含了除0123456789Eπ.()sincostanlnlogn!+-×÷√^以外的字符的str为非法的，返回false
     */
    private boolean checkStr(String str) {
        int i = 0;
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '0' && str.charAt(i) != '1'
                    && str.charAt(i) != '2' && str.charAt(i) != '3'
                    && str.charAt(i) != '4' && str.charAt(i) != '5'
                    && str.charAt(i) != '6' && str.charAt(i) != '7'
                    && str.charAt(i) != '8' && str.charAt(i) != '9'
                    && str.charAt(i) != '.' && str.charAt(i) != '-'
                    && str.charAt(i) != '+' && str.charAt(i) != '×'
                    && str.charAt(i) != '÷' && str.charAt(i) != '√'
                    && str.charAt(i) != '^' && str.charAt(i) != 's'
                    && str.charAt(i) != 'i' && str.charAt(i) != 'n'
                    && str.charAt(i) != 'c' && str.charAt(i) != 'o'
                    && str.charAt(i) != 't' && str.charAt(i) != 'a'
                    && str.charAt(i) != 'l' && str.charAt(i) != 'g'
                    && str.charAt(i) != '(' && str.charAt(i) != ')'
                    && str.charAt(i) != '!' && str.charAt(i) != 'E'
                    && str.charAt(i) != 'π')
                break;
        }
        if (i == str.length()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 整个计算核心，
     * 只要将表达式的整个字符串传入calc().process()就可以实行计算了 算法包括以下几部分：
     *  1、计算部分
     * process(String str) 当然，这是建立在查错无错误的情况下
     * 2、数据格式化 FP(double n) 使数据有相当的精确度
     * 3、阶乘算法 N(double n) 计算n!，将结果返回
     *  4、错误提示 showError(int code ,String str)
     * 将错误返回
     */
    public class calc {

        final int MAXLEN = 500;

        /*
         * 计算表达式 从左向右扫描，数字入number栈，运算符入operator栈
         * +-基本优先级为1，
         * ×÷基本优先级为2，
         * log ln sin cos tan n!基本优先级为3，
         * √^基本优先级为4
         * 括号内层运算符比外层同级运算符优先级高4
         * 当前运算符优先级高于栈顶压栈，
         * 低于栈顶弹出一个运算符与两个数进行运算
         *  重复直到当前运算符大于栈顶
         *   扫描完后对剩下的运算符与数字依次计算
         */
        public void process(String str) {
            int weightPlus = 0, topOp = 0, topNum = 0, flag = 1, weightTemp = 0;
            // weightPlus为同一（）下的基本优先级，weightTemp临时记录优先级的变化
            // topOp为weight[]，operator[]的计数器；topNum为number[]的计数器
            // flag为正负数的计数器，1为正数，-1为负数
            int weight[]; // 保存operator栈中运算符的优先级，以topOp计数
            double number[]; // 保存数字，以topNum计数
            char ch, ch_gai, operator[];// operator[]保存运算符，以topOp计数
            String num;// 记录数字，str以+-×÷()sctgl!√^分段，+-×÷()sctgl!√^字符之间的字符串即为数字
            weight = new int[MAXLEN];
            number = new double[MAXLEN];
            operator = new char[MAXLEN];
            String expression = str;
            StringTokenizer expToken = new StringTokenizer(expression,
                    "+-×÷()sctgl!√^");//"+-×÷()sctgl!√^"参数定界符里面都是分隔符
            int i = 0;

            while (i < expression.length()) {
                ch = expression.charAt(i);
                // 判断正负数
                if (i == 0) {
                    if (ch == '-')
                        flag = -1;
                } else if (expression.charAt(i - 1) == '(' && ch == '-')
                    flag = -1;
                // 取得数字，并将正负符号转移给数字
                if (ch <= '9' && ch >= '0' || ch == '.' || ch == 'E' || ch == 'π') {
                    num = expToken.nextToken();
                    ch_gai = ch;
                    // 取得整个数字
                    while (i < expression.length() && (ch_gai <= '9' && ch_gai >= '0' || ch_gai == '.' || ch_gai == 'E' || ch == 'π')) {
                        ch_gai = expression.charAt(i++);
                    }
                    // 将指针退回之前的位置
                    if (i >= expression.length())
                        i -= 1;
                    else {
                        i -= 2;
                    }
                    if (num.compareTo(".") == 0) {
                        number[topNum++] = 0;
                        // 将正负符号转移给数字
                    } else {
                        //把操作数装进number[]栈里面
                        if (num.compareTo("E") == 0) {
                            number[topNum++] = Math.E * flag;
                            flag = 1;
                        } else if (num.compareTo("π") == 0) {
                            number[topNum++] = Math.PI * flag;
                            flag = 1;
                        } else {
                            number[topNum++] = Double.parseDouble(num) * flag;
                            flag = 1;
                        }
                    }
                }
                // 计算运算符的优先级
                if (ch == '(')
                    weightPlus += 4;
                if (ch == ')')
                    weightPlus -= 4;
                if (ch == '-' && flag == 1 || ch == '+' || ch == '×'
                        || ch == '÷' || ch == 's' || ch == 'c' || ch == 't'
                        || ch == 'g' || ch == 'l' || ch == '!' || ch == '√'
                        || ch == '^') {
                    switch (ch) {
                        // +-的优先级最低，为1
                        case '+':
                        case '-':
                            weightTemp = 1 + weightPlus;
                            break;
                        // x÷的优先级稍高，为2
                        case '×':
                        case '÷':
                            weightTemp = 2 + weightPlus;
                            break;
                        // sincos之类优先级为3
                        case 's':
                        case 'c':
                        case 't':
                        case 'g':
                        case 'l':
                        case '!':
                            weightTemp = 3 + weightPlus;
                            break;
                        // 其余优先级为4
                        // case '^':
                        // case '√':
                        default:
                            weightTemp = 4 + weightPlus;
                            break;
                    }
                    // 如果当前优先级大于堆栈顶部元素，则直接入栈
                    if (topOp == 0 || weight[topOp - 1] < weightTemp) {
                        weight[topOp] = weightTemp;
                        operator[topOp] = ch;
                        topOp++;
                        // 否则将堆栈中运算符逐个取出，直到当前堆栈顶部运算符的优先级小于当前运算符
                    } else {
                        while (topOp > 0 && weight[topOp - 1] >= weightTemp) {
                            switch (operator[topOp - 1]) {
                                // 取出数字数组的相应元素进行运算
                                case '+':
                                    number[topNum - 2] += number[topNum - 1];
                                    break;
                                case '-':
                                    number[topNum - 2] -= number[topNum - 1];
                                    break;
                                case '×':
                                    number[topNum - 2] *= number[topNum - 1];
                                    break;
                                // 判断除数为0的情况
                                case '÷':
                                    if (number[topNum - 1] == 0) {
                                        showError(1, str_old);
                                        return;
                                    }
                                    number[topNum - 2] /= number[topNum - 1];
                                    break;
                                case '√':
                                    if (number[topNum - 1] == 0
                                            || (number[topNum - 2] < 0 && number[topNum - 1] % 2 == 0)) {
                                        showError(2, str_old);
                                        return;
                                    }
                                    number[topNum - 2] = Math.pow(
                                            number[topNum - 2],
                                            1 / number[topNum - 1]);
                                    break;
                                case '^':
                                    number[topNum - 2] = Math.pow(
                                            number[topNum - 2], number[topNum - 1]);
                                    break;
                                // 计算时进行角度弧度的判断及转换
                                // sin
                                case 's':
                                    number[topNum - 1] = Math
                                            .sin((number[topNum - 1] / 180)
                                                    * pi);
                                    topNum++;
                                    break;
                                // cos
                                case 'c':
                                    number[topNum - 1] = Math
                                            .cos((number[topNum - 1] / 180)
                                                    * pi);
                                    topNum++;
                                    break;
                                // tan
                                case 't':
                                    if ((Math.abs(number[topNum - 1]) / 90) % 2 == 1) {
                                        showError(2, str_old);
                                        return;
                                    }
                                    number[topNum - 1] = Math
                                            .tan((number[topNum - 1] / 180)
                                                    * pi);
                                    topNum++;
                                    break;
                                // log
                                case 'g':
                                    if (number[topNum - 1] <= 0) {
                                        showError(2, str_old);
                                        return;
                                    }
                                    number[topNum - 1] = Math
                                            .log10(number[topNum - 1]);
                                    topNum++;
                                    break;
                                // ln
                                case 'l':
                                    if (number[topNum - 1] <= 0) {
                                        showError(2, str_old);
                                        return;
                                    }
                                    number[topNum - 1] = Math
                                            .log(number[topNum - 1]);
                                    topNum++;
                                    break;
                                // 阶乘
                                case '!':
                                    if (number[topNum - 1] > 170) {
                                        showError(3, str_old);
                                        return;
                                    } else if (number[topNum - 1] < 0) {
                                        showError(2, str_old);
                                        return;
                                    }
                                    number[topNum - 1] = N(number[topNum - 1]);
                                    topNum++;
                                    break;
                            }
                            // 继续取堆栈的下一个元素进行判断
                            topNum--;
                            topOp--;
                        }
                        // 将运算符如堆栈
                        weight[topOp] = weightTemp;
                        operator[topOp] = ch;
                        topOp++;
                    }
                }
                i++;
            }
            // 依次取出堆栈的运算符进行运算
            while (topOp > 0) {
                // +-x直接将数组的后两位数取出运算
                switch (operator[topOp - 1]) {
                    case '+':
                        number[topNum - 2] += number[topNum - 1];
                        break;
                    case '-':
                        number[topNum - 2] -= number[topNum - 1];
                        break;
                    case '×':
                        number[topNum - 2] *= number[topNum - 1];
                        break;
                    // 涉及到除法时要考虑除数不能为零的情况
                    case '÷':
                        if (number[topNum - 1] == 0) {
                            showError(1, str_old);
                            return;
                        }
                        number[topNum - 2] /= number[topNum - 1];
                        break;
                    case '√':
                        if (number[topNum - 1] == 0
                                || (number[topNum - 2] < 0 && number[topNum - 1] % 2 == 0)) {
                            showError(2, str_old);
                            return;
                        }
                        number[topNum - 2] = Math.pow(number[topNum - 2],
                                1 / number[topNum - 1]);
                        break;
                    case '^':
                        number[topNum - 2] = Math.pow(number[topNum - 2],
                                number[topNum - 1]);
                        break;
                    // sin
                    case 's':
                        number[topNum - 1] = Math
                                .sin((number[topNum - 1] / 180) * pi);
                        topNum++;
                        break;
                    // cos
                    case 'c':
                        number[topNum - 1] = Math
                                .cos((number[topNum - 1] / 180) * pi);
                        topNum++;
                        break;
                    // tan
                    case 't':
                        if ((Math.abs(number[topNum - 1]) / 90) % 2 == 1) {
                            showError(2, str_old);
                            return;
                        }
                        number[topNum - 1] = Math
                                .tan((number[topNum - 1] / 180) * pi);
                        topNum++;
                        break;
                    // 对数log
                    case 'g':
                        if (number[topNum - 1] <= 0) {
                            showError(2, str_old);
                            return;
                        }
                        number[topNum - 1] = Math.log10(number[topNum - 1]);
                        topNum++;
                        break;
                    // 自然对数ln
                    case 'l':
                        if (number[topNum - 1] <= 0) {
                            showError(2, str_old);
                            return;
                        }
                        number[topNum - 1] = Math.log(number[topNum - 1]);
                        topNum++;
                        break;
                    // 阶乘
                    case '!':
                        if (number[topNum - 1] > 170) {
                            showError(3, str_old);
                            return;
                        } else if (number[topNum - 1] < 0) {
                            showError(2, str_old);
                            return;
                        }
                        number[topNum - 1] = N(number[topNum - 1]);
                        topNum++;
                        break;
                }
                // 取堆栈下一个元素计算
                topNum--;
                topOp--;
            }
            // 如果是数字太大，提示错误信息
            if (number[0] > 7.3E306) {
                showError(3, str_old);
                return;
            }
            // 输出最终结果
            prefix.setText(str_old);
            tvShow.setText(" = " + String.valueOf(FP(number[0])));
        }

        /*
         * FP = floating point 控制小数位数，达到精度 否则会出现
         * 0.6-0.2=0.39999999999999997的情况，用FP即可解决，使得数为0.4 本格式精度为15位
         */
        public double FP(double n) {
            // NumberFormat format=NumberFormat.getInstance(); //创建一个格式化类f
            // format.setMaximumFractionDigits(18); //设置小数位的格式
            DecimalFormat format = new DecimalFormat("0.###");
            return Double.parseDouble(format.format(n));
        }

        /*
         * 阶乘算法
         */
        public double N(double n) {
            int i = 0;
            double sum = 1;
            // 依次将小于等于n的值相乘
            for (i = 1; i <= n; i++) {
                sum = sum * i;
            }
            return sum;
        }

        /*
         * 错误提示，按了"="之后，若计算式在process()过程中，出现错误，则进行提示
         */
        public void showError(int code, String str) {
            String message = "";
            switch (code) {
                case 1:
                    message = "零不能作除数";
                    break;
                case 2:
                    message = "函数格式错误";
                    break;
                case 3:
                    message = "值太大了，超出范围";
            }
            tvShow.setText("\"" + str + "\"" + ": " + message);
        }
    }
}

