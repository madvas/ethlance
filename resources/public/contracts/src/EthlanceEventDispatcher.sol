pragma solidity ^0.4.24;

import "proxy/MutableForwarder.sol";

/*
  EventDispatcher deployment should make use of the mutable forwarder.
 */

/// @title Dynamic Event Dispatch
contract EthlanceEventDispatcher {
    event EthlanceEvent(address indexed _address,
			string event_name,
			uint event_version,
			uint timestamp,
			uint[] event_data);
		   

    /// @dev Emit the dynamic Ethlance Event.
    /// @param event_name - Name of the event.
    /// @param event_version - Version of the event.
    /// @param event_data - Array of data within the event.
    function fireEvent(string event_name,
                       uint event_version,
                       uint[] event_data)
        public {

        emit EthlanceEvent(msg.sender,
			   event_name,
			   event_version,
			   now,
			   event_data);
    }
}