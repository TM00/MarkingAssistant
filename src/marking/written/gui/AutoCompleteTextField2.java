package marking.written.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("restriction")
public class AutoCompleteTextField2 extends TextField {

	/** The existing autocomplete entries. */
	private final SortedSet<String> entries;
	/** The popup used to select an entry. */
	private ContextMenu entriesPopup;

	private boolean caseSensitive; // false by default
	private ContextMenuContent.MenuItemContainer itemSelected=null;

	/** Construct a new AutoCompleteTextField. */
	public AutoCompleteTextField2() {
		super();
		entries = new TreeSet<>();
		entriesPopup = new ContextMenu();
		caseSensitive=false;
		textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
				if (getText().length() == 0)
				{
					entriesPopup.hide();
				} else
				{
					LinkedList<String> searchResult = new LinkedList<>();
					if(caseSensitive){
						searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
					}
					else{
						// Convert all to upper case...
						SortedSet<String> entries2 = new TreeSet<>();

						for(String st: entries){
							entries2.add(st.toUpperCase());
						}

						searchResult.addAll(entries.subSet(getText().toUpperCase(), getText().toUpperCase() + Character.MAX_VALUE));
					}

					if (entries.size() > 0)
					{
						populatePopup(searchResult);
						if (!entriesPopup.isShowing())
						{
							entriesPopup.show(AutoCompleteTextField2.this, Side.BOTTOM, 0, 0);
						}
					} else
					{
						entriesPopup.hide();
					}
				}
			}
		});

		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
				entriesPopup.hide();
			}
		});

		addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {

					TextFieldSkin  skin = (TextFieldSkin) getSkin();
					if (skin.getBehavior() instanceof TextFieldBehavior) {
						TextFieldBehavior behavior = (TextFieldBehavior) skin.getBehavior();


						behavior.callAction("TraverseNext");

						event.consume();
					}

				}
			}
		});


		// Tried 16/2/2017 - failed
		entriesPopup.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				System.out.println("KeyEvent called_1");

				if (event.getCode() == KeyCode.TAB) {


				}


			}
		});
	}

	private void setSelectedSuggestionAsTextAndMoveFocus(){
		//		entriesPopup.g
	}

	/**
	 * Get the existing set of autocomplete entries.
	 * @return The existing autocomplete entries.
	 */
	public SortedSet<String> getEntries() { return entries; }

	/**
	 * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
	 * @param searchResult The set of matching strings.
	 */
	private void populatePopup(List<String> searchResult) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		// If you'd like more entries, modify this line.
		int maxEntries = 10;
		int count = Math.min(searchResult.size(), maxEntries);
		for (int i = 0; i < count; i++)
		{
			final String result = searchResult.get(i);
			Label entryLabel = new Label(result);


			CustomMenuItem item = new CustomMenuItem(entryLabel, true);

			item.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent actionEvent) {
					System.out.println("Handling action event");
					setText(result);

					entriesPopup.hide();


					//	fireActionEvent();
					//	nextFocus();

				}
			});


			// NOT WORKING!!
			item.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) { 
					System.out.println("KeyEvent called");
					if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB)  {
						setText(result);
						entriesPopup.hide();
					}
				};  
			});

			menuItems.add(item);
		}
		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);



	}

	private void fireActionEvent(){
		this.fireEvent(new ActionEvent());
	}

	private void nextFocus(){
		System.out.println("NF called");

		// NOT WORKING . . . 

		/*	ObservableList<Node> children = this.getParent().getChildrenUnmodifiable();
         int idx = children.indexOf(this);
         if (idx >= 0) {
           for (int i = idx + 1; i < children.size(); i++) {
             if (children.get(i).isFocusTraversable()) {
               children.get(i).requestFocus();
               break;
             }
           }
           for (int i = 0; i < idx; i++) {
             if (children.get(i).isFocusTraversable()) {
               children.get(i).requestFocus();
               break;
             }
           }
         }
         if( this.getSkin() instanceof BehaviorSkinBase) {
             ((BehaviorSkinBase) this.getSkin()).getBehavior().traverseNext();  
         }*/
		// traverse focus if pressing TAB and insert tab if pressing CONTROL+TAB
		if( this.getSkin() instanceof BehaviorSkinBase) {
			((BehaviorSkinBase<?, ?>) this.getSkin()).getBehavior().traverseNext();  
		}

	}

	public void setCaseSensitive(boolean b){
		this.caseSensitive=b;
	}
}
