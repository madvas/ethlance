// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.0;
pragma experimental ABIEncoderV2;

import "./Ethlance.sol";
// import "../token/ApproveAndCallFallback.sol";
import "@openzeppelin/contracts/token/ERC721/IERC721Receiver.sol";
import "@openzeppelin/contracts/token/ERC1155/IERC1155Receiver.sol";
import "@openzeppelin/contracts/token/ERC20/IERC20.sol";


/**
 * @dev Job contract on Ethlance
 * Job facilitates value transfers between job creator, one or multiple candidates
 * and optionally an arbiter.
 * Every new Job contract is created as a proxy contract.
 */

contract Job {

  uint public constant version = 1; // current version of {Job} smart-contract
  Ethlance public ethlance; // Stores address of {Ethlance} smart-contract so it can emit events there

  /**
   * @dev Contract initialization
   * It is manually called instead of native contructor,
   * because this contract is used through a proxy.
   * This function cannot be called twice.
   *
   * It stores passed arguments for later usage
   * It validates if `_offeredValues` have been actually transferred into this contract
   *
   * Requirements:
   *
   * - `_ethlance` cannot be empty
   * - `_creator` cannot be empty
   * - `_offeredValues` cannot be empty
   *
   * TODO: Needs implementation
   */
  // From user -> Ethlance contract (this address never changes) |ethlance proxy| -> |ethlance impl|
  // From Ethlance contract -> job contract |job proxy (with state)| -> |job impl|
  // When the createJob can be done in 1 transaction (User -> Ethlance ())
  // Ethlance contract will give approval to Job contract, Job contract transfers the tokens to itself
  //   - this is inefficient and thus better is if the Ethlance makes the transfer
  //
  // In all cases: ethlance contract transfers to job contract
  //   - will do in 1 transaction when possible (approving the other transfer)
  //   - if not, then in 2 (take preapproved from user -> Ethlance, then Ethlance -> Job)
  // Job contract just validates that it has the contract
  //
  // 1. User approves first the ERC20 transfer to Ethlance contract
  // 2. Then Ethlance does the transfer of the preapproved tokens
  //
  // First make ERC20 work (approve transfer from the test, then call Ethlance#createJob)
  //
  // We deploy new Job proxy for each new job created on the Ethlance
  // These proxies hold the state for that particular job
  // but they point to the same implementation deployment (contract)
  // This way it's cheaper as during job creation only the proxy (which is
  // basically pointer + data) needs to be deployed
  function initialize(
    Ethlance _ethlance,
    address _creator,
    Ethlance.JobType _jobType,
    Ethlance.TokenValue[] memory _offeredValues,
    address[] calldata _invitedArbiters
  ) external {
    // require(address(_ethlance) != address(0), "Ethlance can't be null");
    // require(_creator != address(0), "Creator can't be null");
    // require(_offeredValues.length > 0, "You must offer some tokens as pay");

    // ethlance = _ethlance;
    // for(uint i = 0; i < _offeredValues.length; i++) {
    //   Ethlance.TokenValue memory offerInfo = _offeredValues[i];
    //   // TODO: need to support (add checks for) all TokenTypes (ETH, ERC20, ...)
    //   uint offeredAmount = offerInfo.value;
    //   IERC20 offeredToken = IERC20(offerInfo.token.tokenContract.tokenAddress);
    //   uint depositedAmount = offeredToken.balanceOf(address(this)); // this refers to the Job proxy

    //   require(depositedAmount == offeredAmount);
    // }
  }


  /**
   * @dev Sets quote for arbitration requested by arbiter for his services
   *
   * It stores passed arguments for later usage
   * It validates if `_offeredValues` have been actually transferred into this contract
   *
   * Requirements:
   * - `msg.sender` must be among invited arbiters
   * - `_quote` cannot be empty
   *
   * Emits {QuoteForArbitrationSet} event
   *
   * TODO: Needs implementation
   */
  function setQuoteForArbitration(
    Ethlance.TokenValue[] memory _quote
  ) external {
  }


  /**
   * @dev It is called by job creator when he decides to accept an quote from an arbiter
   * It checks if `_transferredValue` matches the quote requested by an arbiter
   * It transfers the value to the arbiter's address
   *
   * This function is not meant to be called directly, but via token received callbacks
   *
   * Requirements:
   * - Can only be called by job creator
   * - `_arbiter` must be among arbiters who already set their quotes
   * - `_transferredValue` must match quote requested by an arbiter
   * - Only 1 arbiter can be accepted. Further accepts should revert.
   *
   * Emits {QuoteForArbitrationAccepted} event
   *
   * TODO: Needs implementation
   */
  function _acceptQuoteForArbitration(
    address _arbiter,
    Ethlance.TokenValue[] memory _transferredValue
  ) internal {
  }


  /**
   * @dev It is called by job creator when he allows a new candidate to start invoicing for this job
   *
   * Requirements:
   * - Can only be called by job creator
   * - Can be called only when {Ethlance.JobType} is GIG
   * - `_candidate` cannot be empty
   * - same `_candidate` cannot be added twice
   *
   * Emits {CandidateAdded} event
   *
   * TODO: Needs implementation
   */
  function addCandidate(
    address _candidate
  ) external {
  }


  /**
   * @dev Function called by candidate to create an invoice to be paid
   *
   * Requirements:
   * - If {Ethlance.JobType} is GIG, `msg.sender` must be among added candidates
   * - If {Ethlance.JobType} is BOUNTY, anybody can call this function
   * - `_invoicedValue` cannot be empty
   * - `_ipfsData` cannot be empty
   *
   * Emits {InvoiceCreated} event
   * See spec :ethlance/invoice-created for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function createInvoice(
    Ethlance.TokenValue[] memory _invoicedValue,
    bytes memory _ipfsData
  ) external {
  }


  /**
   * @dev Transfers invoiced value from this contract to the invoicer's address
   *
   * Requirements:
   * - Can be called only by job creator
   * - `_invoiceId` must be valid invoiceId
   * - `_ipfsData` can be empty
   *
   * Emits {InvoicePaid} event
   * See spec :ethlance/invoice-paid for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function payInvoice(
    uint _invoiceId,
    bytes memory _ipfsData
  ) external {
  }


  /**
   * @dev Cancels existing invoice
   *
   * Requirements:
   * - Can be called only by invoicer
   * - `_invoiceId` must be valid invoiceId and still not paid
   * - `_ipfsData` can be empty
   *
   * Emits {InvoiceCanceled} event
   * See spec :ethlance/invoice-canceled for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function cancelInvoice(
    uint _invoiceId,
    bytes memory _ipfsData
  ) external {
  }


  /**
   * @dev Adds funds to the job smart-contract
   * Funds can be added by anyone and smart-contract keeps track of which address funded how much, so later
   * funds can be withdrawn by their original owner if desired
   *
   * This function is not meant to be called directly, but via token received callbacks
   *
   * Requirements:
   * - `_funder` cannot be empty
   * - `_fundedValue` cannot be empty
   *
   * Emits {FundsAdded} event
   * See spec :ethlance/funds-added for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function _addFunds(
    address _funder,
    Ethlance.TokenValue[] memory _fundedValue
  ) internal {
  }


  /**
   * @dev It joins together `{_addFunds}` and `{payInvoice}` calls
   *
   * This function is not meant to be called directly, but via token received callbacks
   * TODO: Needs implementation
   */
  function _addFundsAndPayInvoice(
    Ethlance.TokenValue[] memory _fundedValue,
    uint _invoiceId
  ) internal {
  }


  /**
   * @dev Withdraws funds back to the original funder
   *
   * Requirements:
   * - Funds cannot be withdrawn if there's raised dispute or unpaid invoice
   * - `msg.sender` can only withdraw exact amount he's previously funded if it's still available in contract
   *
   * Emits {FundsWithdrawn} event
   * See spec :ethlance/funds-withdrawn for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function withdrawFunds(
  ) external {
  }


  /**
   * @dev Raises a dispute between job creator and candidate
   *
   * Requirements:
   * - Only creator of the invoice with `invoiceId` can call this function
   * - Dispute can't be raised twice for the same `invoiceId`
   *
   * Emits {DisputeRaised} event
   * See spec :ethlance/dispute-raised for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function raiseDispute(
    uint _invoiceId,
    bytes memory _ipfsData
  ) external {
  }


  /**
   * @dev Resolves a dispute between job creator and candidate
   * It tramsfers `_valueForInvoicer` from this contract into invoicer's address
   *
   * Requirements:
   * - Can be called only by arbiter accepted for this job
   * - Dispute can't be raised twice for the same `invoiceId`
   * - `_ipfsData` cannot be empty
   *
   * Emits {DisputeResolved} event
   * See spec :ethlance/dispute-resolved for the format of _ipfsData file
   * TODO: Needs implementation
   */
  function resolveDispute(
    uint _invoiceId,
    Ethlance.TokenValue[] memory _valueForInvoicer,
    bytes memory _ipfsData
  ) external {
  }


  /**
   * @dev This function is called automatically when this contract receives approval for ERC20 MiniMe token
   * It calls either {_acceptQuoteForArbitration} or {_addFunds} or {_addFundsAndPayInvoice} based on decoding `_data`
   * TODO: Needs implementation
   */
  function receiveApproval(
    address _from,
    uint256 _amount,
    address _token,
    bytes memory _data
  ) external {
  }


  /**
   * @dev This function is called automatically when this contract receives ERC721 token
   * It calls either {_acceptQuoteForArbitration} or {_addFunds} or {_addFundsAndPayInvoice} based on decoding `_data`
   * TODO: Needs implementation
   */
  function onERC721Received(
    address _operator,
    address _from,
    uint256 _tokenId,
    bytes memory _data
  ) public returns (bytes4) {
    return bytes4(keccak256("onERC721Received(address,address,uint256,bytes)"));
  }


  /**
   * @dev This function is called automatically when this contract receives ERC1155 token
   * It calls either {_acceptQuoteForArbitration} or {_addFunds} or {_addFundsAndPayInvoice} based on decoding `_data`
   * TODO: Needs implementation
   */
  function onERC1155Received(
    address _operator,
    address _from,
    uint256 _id,
    uint256 _value,
    bytes calldata _data
  ) external returns (bytes4) {
    return bytes4(keccak256("onERC1155Received(address,address,uint256,uint256,bytes)"));
  }


  /**
   * @dev This function is called automatically when this contract receives multiple ERC1155 tokens
   * It calls either {_acceptQuoteForArbitration} or {_addFunds} or {_addFundsAndPayInvoice} based on decoding `_data`
   * TODO: Needs implementation
   */
  function onERC1155BatchReceived(
    address _operator,
    address _from,
    uint256[] calldata _ids,
    uint256[] calldata _values,
    bytes calldata _data
  ) external returns (bytes4) {
    return bytes4(keccak256("onERC1155BatchReceived(address,address,uint256[],uint256[],bytes)"));
  }


  /**
   * @dev This function is called automatically when this contract receives ETH
   * It calls either {_acceptQuoteForArbitration} or {_addFunds} or {_addFundsAndPayInvoice} based on decoding `msg.data`
   * TODO: Needs implementation
   */
  receive(
  ) external payable {
  }


}
