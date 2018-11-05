import java.util.*;
import java.math.*;
import java.io.*;

public class bits {
	final int MOD = (int) 1e9 + 9;
	public bits() {
		FS scan = new FS();
		int k = scan.nextInt(), b = scan.nextInt();
		int[] pow2 = new int[b + 1];
		pow2[0] = 1;
		for(int i = 1 ; i <= b ; i++)
			pow2[i] = (pow2[i - 1] << 1) % k;
		long[][] dp = new long[b + 1][k];
		long[][] cdp = new long[b + 1][k];
		for(int rem = 1 ; rem < k ; rem++)
			dp[b][rem] = -1;
		cdp[b][0] = 1;
		for(int at = b - 1 ; at >= 0 ; at--) {
			for(int rem = 0 ; rem < k ; rem++) {
				int nextrem = (rem + pow2[at]) % k;
				cdp[at][rem] += cdp[at + 1][nextrem];
				cdp[at][rem] += cdp[at + 1][rem];
				cdp[at][rem] %= MOD;
				boolean found = false;
				if(dp[at + 1][nextrem] != -1) {
					dp[at][rem] = cdp[at + 1][nextrem] + dp[at + 1][nextrem];
					found = true;
				}
				if(dp[at + 1][rem] != -1) {
					dp[at][rem] += dp[at + 1][rem];
					found = true;
				}
				dp[at][rem] %= MOD;
				dp[at][rem] = !found ? -1 : dp[at][rem];
			}
		}
		System.out.println(dp[0][0]);
	}
	class FS {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");
		public String next() {
			while(!st.hasMoreTokens()) {
				try { st = new StringTokenizer(br.readLine()); }
				catch(Exception e) { e.printStackTrace(); }
			}
			return st.nextToken();
		}
		public int nextInt() { return Integer.parseInt(next()); }
	}
	public static void main(String[] args) { new bits(); }
}
