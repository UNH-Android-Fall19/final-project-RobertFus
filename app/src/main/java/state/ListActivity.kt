package state

import `interface`.StoreState
import actions.Actions
import models.Store
import models.StoreType

class StoreList(val store: List<Store>):
    StoreState {
    override fun consumeAction(action: Actions): StoreState {
        return when(action) {
            is Actions.AddStoreClicked -> AddStore(store)
            is Actions.SubmitStoreClicked -> { val newSandwich = Store(action.StoreName, StoreType.GRINDER)
                StoreList(store + newSandwich)
            }
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}