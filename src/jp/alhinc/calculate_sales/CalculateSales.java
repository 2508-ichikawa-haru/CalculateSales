package jp.alhinc.calculate_sales;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CalculateSales {

	// 支店定義ファイル名
	private static final String FILE_NAME_BRANCH_LST = "branch.lst";

	// 支店別集計ファイル名
	private static final String FILE_NAME_BRANCH_OUT = "branch.out";

	// エラーメッセージ
	private static final String UNKNOWN_ERROR = "予期せぬエラーが発生しました";
	private static final String FILE_NOT_EXIST = "支店定義ファイルが存在しません";
	private static final String FILE_INVALID_FORMAT = "支店定義ファイルのフォーマットが不正です";

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void main(String[] args) {
		// 支店コードと支店名を保持するMap
		Map<String, String> branchNames = new HashMap<>();
		// 支店コードと売上金額を保持するMap
		Map<String, Long> branchSales = new HashMap<>();

		// 支店定義ファイル読み込み処理
		if(!readFile(args[0], FILE_NAME_BRANCH_LST, branchNames, branchSales)) {
			return;
		}


		// ※ここから集計処理を作成してください。(処理内容2-1、2-2)
		File[] files = new File(args[0]).listFiles();

		List<File> rcdFiles = new ArrayList<File>();

		for(int i = 0; i < files.length; i++) {//要素の数だけ繰り返す
			String fileName = files[i].getName();//files[i]の中のファイル型のi番目の名前を取り出す
			if(fileName.matches("[0-9]{8}[.]rcd$")) {//取り出した名前のうち、""内の形に合うものをrcdFilesに入れる
				rcdFiles.add(files[i]);
			}
		}
		//
		for(int i = 0; i < rcdFiles.size(); i++) {
			// ファイルの読込をしたい
			// BufferedReaderのreadLineを使いたい
			// BufferedReaderが必要
			//FileReaderが必要
			// Fileが必要
			BufferedReader br = null;
			//
			String fileName = rcdFiles.get(i).getName();
			//String型「fileName」にrcdFilesのi番目のファイル名をString型で代入する

			try {
				File file = new File(args[0], fileName);
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);

				String line;
				// 一行ずつ読み込む
				List<String> saleList = new ArrayList<String>();//リスト「SaleList」作成

				while((line = br.readLine()) != null) {
					// 読み込んだ内容をリストに入れる

					saleList.add(line); //リスト「SaleList」に一行ずつ読み込んだデータを入れる
				}


				//売上ファイルから読み込んだ売上金額をMapに加算していくために、型の変換を行います。
				//※詳細は後述で説明
				long fileSale = Long.parseLong(saleList.get(1));

				//読み込んだ売上⾦額を加算します。
				//※詳細は後述で説明
				Long saleAmount = branchSales.get(saleList.get(0)) + fileSale;

				//加算した売上⾦額をMapに追加します。
				 branchSales.put(saleList.get(0), saleAmount); //SaleListに入れた0番目（支店コード）とLong型に
				 												//変換して加算した1番目（売上）をmapに追加

			}catch(IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return ;

			}finally {
				if(br != null) {
					try {
						br.close();
					}catch(IOException e) {
						System.out.println(UNKNOWN_ERROR);
						return ;
					}
				}
			}
		}
		// 支店別集計ファイル書き込み処理
		if(!writeFile(args[0], FILE_NAME_BRANCH_OUT, branchNames, branchSales)) {
			return;
		}
	}

	/**
	 * 支店定義ファイル読み込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 読み込み可否
	 */
	private static boolean readFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		BufferedReader br = null;

		try {
			//
			File file = new File(path, fileName);
			//
			FileReader fr = new FileReader(file);
			//
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {
				String[] items = line.split(",");
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
				System.out.println(line);
				branchNames.put(items[0], items[1]);
				branchSales.put(items[0], 0L);
			}

		} catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if(br != null) {
				try {
					// ファイルを閉じる
					br.close();
				} catch(IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 支店別集計ファイル書き込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 書き込み可否
	 */
	private static boolean writeFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		// ※ここに書き込み処理を作成してください。(処理内容3-1)
		BufferedWriter bw = null;
		try {
			File file = new File(path, fileName);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			 //拡張for文でMapからKeyの一覧を取得してKeyの数だけ繰り返す
			for (String key :  branchSales.keySet()) {
				bw.write(key + "," + branchNames.get(key) + "," +  branchSales.get(key));//keyにあわせて、それぞれのmapから
																						 //keyに付随するvalueの値を取り出す
				bw.newLine(); //改行する
			}

		}catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;

		}finally {
			if(bw != null) {
				try {
					bw.close();
				} catch(IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}

}
