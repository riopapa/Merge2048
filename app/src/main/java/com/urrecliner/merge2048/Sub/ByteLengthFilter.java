package com.urrecliner.merge2048.Sub;

import android.text.InputFilter;
import android.text.Spanned;

import java.io.UnsupportedEncodingException;

class ByteLengthFilter implements InputFilter {
    private final String mCharset; // 인코딩 문자셋 "euc-kr"
    protected int mMaxByte; // 입력가능한 최대 바이트 길이
    public ByteLengthFilter(int maxByte, String charset) {
        this.mMaxByte = maxByte;
        this.mCharset = charset;
    }

    /**
     * 이 메소드는 입력/삭제 및 붙여넣기/잘라내기할 때마다 실행된다. - charIn : 새로 입력/붙여넣기 되는
     * 문자열(삭제/잘라내기 시에는 "") - charNow : 변경 전 원래 문자열
     */

    public CharSequence filter(CharSequence charIn, int start, int finish, Spanned charNow,
                               int destStart,int destFinish) {
        // 변경 후 예상되는 문자열
        String expected = "";
        expected += charNow.subSequence(0, destStart);
        expected += charIn.subSequence(start, finish);
        expected += charNow.subSequence(destFinish, charNow.length());
        int keep = calculateMaxLength(expected) - (charNow.length() - (destFinish - destStart));
        if(keep < 0) //keep -값이 나올경우를 대비한 방어코드
            keep = 0;
        int Rekeep = plusMaxLength(charNow.toString(), charIn.toString(), start);

        if (keep <= 0 && Rekeep <= 0) {
            return ""; // charIn 입력 불가(원래 문자열 변경 없음)
        } else if (keep >= finish - start) {
            return null; // keep original. charIn 그대로 허용
        } else {
            if( charNow.length() == 0 && Rekeep <= 0 ) //기존의 내용이 없고, 붙여넣기 하는 문자바이트가 넘을경우
                return charIn.subSequence(start, start + keep);
            else if(Rekeep <= 0)  //엔터가 들어갈 경우 keep이 0이 되어버리는 경우가 있음
                return charIn.subSequence(start, start + (charIn.length()-1));
            else
                return charIn.subSequence(start, start + Rekeep); // source중 일부만입력 허용
        }
    }
    /**
     * 붙여넣기 시 최대입력가능한 길이 수 구하는 함수
     * 숫자와 문자가 있을경우 길이수의 오차가 있음. While문으로 오차범위를 줄여줌
     * @param expected  : 현재 입력되어 있는 문자
     * @param source    : 붙여넣기 할 문자
     * @param start     : start point
     * @return keep     : length
     */
    protected int plusMaxLength( String expected, String source, int start ) {
        int keep = source.length();
        int maxByte = mMaxByte - getByteLength(expected); //입력가능한 byte

        while (getByteLength(source.subSequence(start, start + keep).toString()) > maxByte) {
            keep--;
        }
        return keep;
    }

    /**
     * 입력가능한 최대 문자 길이(최대 바이트 길이와 다름!).
     */

    protected int calculateMaxLength(String expected) {
        int expectedByte = getByteLength(expected);
        if (expectedByte == 0) {
            return 0;
        }
        return mMaxByte - (getByteLength(expected) - expected.length());
    }

    /**
     * 문자열의 바이트 길이. 인코딩 문자셋에 따라 바이트 길이 달라짐.
     *
     * @param str
     * @return
     */

    private int getByteLength(String str) {
        try {
            return str.getBytes(mCharset).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mMaxByte;
    }
}