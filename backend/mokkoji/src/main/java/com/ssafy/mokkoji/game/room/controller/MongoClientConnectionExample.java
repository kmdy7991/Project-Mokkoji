package com.ssafy.mokkoji.game.room.controller;



import java.util.Arrays;
import java.util.Scanner;

public class MongoClientConnectionExample {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String o = sc.nextLine();
        System.out.println(o);
        System.out.println(o.equals("가나다"));
        System.out.println(findWord(o,"가나다"));
    }
    static boolean findWord(String origin, String find) {
        int originIndex;
        int findIndex;
        int originLen = origin.length();
        int findLen = find.length();
        // 한글은 유니코드때문에 MAX_VALUE로 배열 생성함
        int[] table = new int[Character.MAX_VALUE + 1];

        // 배열의 요소 초기화
        Arrays.fill(table, findLen);

        // pattern에 있는 문자에 대해 table 값 변경
        for (int i = 0; i < findLen; i++) {
            table[find.charAt(i)] = findLen - 1 - i;
        }

        // 같은 지점에서 시작하여 오른쪽에서 왼쪽으로 탐색
        originIndex = findLen - 1;

        while (originIndex < originLen) {
            findIndex = findLen - 1;

            // dest의 문자와 pattern의 문자가 같다면 오른쪽에서 왼쪽으로 탐색함
            while (origin.charAt(originIndex) == find.charAt(findIndex)) {
                if (findIndex == 0)
                    return true;
                originIndex--;
                findIndex--;
            }

            // dest의 문자와 pattern의 문자가 다르다면 jump (jump 기준은 origin index가 가르키는 문자가 find에 존재한다면 find와 originindex가 가르키는 값까지 jump
            // 아니라면 findLen에서 findIndex만큼 점프
            originIndex += (table[origin.charAt(originIndex)] > findLen - findIndex) ? table[origin.charAt(originIndex)] : findLen - findIndex;
        }
        return false;
    }
}
