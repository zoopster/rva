package com.risevision.ui.server.rpc;

import java.util.ArrayList;
import java.util.UUID;

import com.risevision.common.client.info.ScheduleInfo;
import com.risevision.common.client.info.TimeLineInfo;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.ScheduleAttribute;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.DistributionCheckInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.DistributionCheckInfo.DistributionCheckDisplayInfo;
import com.risevision.ui.client.common.info.DistributionCheckInfo.DistributionCheckScheduleInfo;
import com.risevision.ui.client.common.service.ScheduleService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.utils.ServerUtils;
import com.risevision.ui.server.utils.TimeLineUtils;

import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ScheduleServiceImpl extends RiseRemoteServiceServlet implements ScheduleService {

//	public SchedulesInfo getSchedules(SchedulesInfo schedulesInfo) throws ServiceFailedException {
//		String bookmark = null;
//
//		//PART 1 - get bookmarks
//		String bookmarkUrl = createSchedulesBookmarksResource(schedulesInfo);
//		Document d1;
//
//		d1 = get(bookmarkUrl);
//		if (d1 != null)
//			bookmark = ServerUtils.getGridBookmark(d1, schedulesInfo);
//		
//		//PART 2 - get schedules
//
//		if (bookmark != null)
//		{
//			String pageUrl = createSchedulesResource(schedulesInfo.getCompanyId(), bookmark);
//			Document d;
//
//			d = get(pageUrl);
//
//			if (d != null) {
//				ArrayList<ScheduleInfo> schedules = parseGetSchedules(d);
//				schedulesInfo.setSchedules(schedules);
//				return schedulesInfo;
//			}
//		}		
//		schedulesInfo.setSchedules(null);
//		return schedulesInfo;
//	}

	public ScheduleInfo getSchedule(String companyId, String scheduleId) throws ServiceFailedException {
		
		String url = createScheduleResource(companyId, scheduleId);

		Document d = get(url);
		ScheduleInfo schedule = null;
		if (d != null)
			schedule = DocToSchedule(d);
		return schedule;
	}
	
	public RpcResultInfo checkDistribution(String companyId, ScheduleInfo schedule) throws ServiceFailedException {
		if ((schedule.getId() == null) || (schedule.getId().isEmpty()))
			schedule.setId(UUID.randomUUID().toString());
		String url = createCheckDistributionResource(companyId, schedule.getId());
		
		Form form = new Form();

		form.add(ScheduleAttribute.DISTRIBUTE_TO_ALL, ServerUtils.BooleanToStr(schedule.getDistributionToAll()));
		form.add(ScheduleAttribute.DISTRIBUTION, ServerUtils.ListToString(schedule.getDistribution(),","));
		
		Document d = post(url, form);
		RpcResultInfo result = new RpcResultInfo();
		if (d != null) {
			DistributionCheckInfo distributionCheck = docToDistributionCheck(d);
			
			if (distributionCheck != null) {
				String resultString = distributionCheck.parseDistributionCheck();
				if (!resultString.isEmpty()){
					result.setErrorMessage(resultString);
				}
			}
		}
		return result;
	}

	public RpcResultInfo putSchedule(String companyId, ScheduleInfo schedule) throws ServiceFailedException {
		// new user- generate userId
		if ((schedule.getId() == null) || (schedule.getId().isEmpty()))
			schedule.setId(UUID.randomUUID().toString());
		String url = createScheduleResource(companyId, schedule.getId());

		Form form = new Form();

		form.add(ScheduleAttribute.ID, schedule.getId());
		form.add(ScheduleAttribute.NAME, schedule.getName());
		TimeLineUtils.addToForm(form, schedule.getTimeline());
		form.add(ScheduleAttribute.DISTRIBUTE_TO_ALL, ServerUtils.BooleanToStr(schedule.getDistributionToAll()));
		form.add(ScheduleAttribute.DISTRIBUTION, ServerUtils.ListToString(schedule.getDistribution(),","));

		form.add(ScheduleAttribute.CONTENT, schedule.getContent());
		
//		form.add(ScheduleAttribute.TRANSITION, schedule.getTransition());
//		form.add(ScheduleAttribute.SCALE, schedule.getScale());
//		form.add(ScheduleAttribute.POSITION, schedule.getPosition());

		put(url, form);
		
		//putPlayListItems(companyId, schedule.getId(), schedule.getPlayListItems());
		return new RpcResultInfo(schedule.getId());
	}

	public RpcResultInfo deleteSchedule(String companyId, String scheduleId) throws ServiceFailedException {
		// new user- generate userId
		if ((scheduleId == null) || (scheduleId.isEmpty()))
			return new RpcResultInfo("null","Shedule ID is null or enmty");

		String url = createScheduleResource(companyId, scheduleId);

		delete(url);
		return new RpcResultInfo();
	}

//	public RpcResultInfo putScheduleItem(String companyId, String scheduleId, PlaylistItemInfo scheduleItem) throws ServiceFailedException {
//		String url = createPlayListItemResource(companyId, scheduleId, scheduleItem.getId());
//
//		Form form = new Form();
//
//		form.add(ScheduleItemAttribute.NAME, scheduleItem.getName());
//		form.add(ScheduleItemAttribute.DURATION, scheduleItem.getDuration());
//		form.add(ScheduleItemAttribute.TYPE, scheduleItem.getType());
//		form.add(ScheduleItemAttribute.URL, scheduleItem.getObjectData());
////		form.add(ScheduleItemAttribute.OBJECTDATA, playListItem.get());
//		form.add(ScheduleItemAttribute.OBJECT_REFERENCE, scheduleItem.getObjectRef());
//		TimeLineUtils.addToForm(form, scheduleItem.getTimeline());
//		form.add(ScheduleItemAttribute.DISTRIBUTE_TO_ALL, ServerUtils.BooleanToStr(scheduleItem.getDistributionToAll()));
//		form.add(ScheduleItemAttribute.DISTRIBUTION, ServerUtils.ListToString(scheduleItem.getDistribution(),","));
//
////		form.add(ScheduleItemAttribute.TRANSITION, scheduleItem.getTransition());
////		form.add(ScheduleItemAttribute.SCALE, scheduleItem.getScale());
////		form.add(ScheduleItemAttribute.POSITION, scheduleItem.getPosition());
//
//		put(url, form);
//		
//		return new RpcResultInfo(scheduleItem.getId());
//	}

	//delete all schedule Items
//	public RpcResultInfo deleteScheduleItems(String companyId, String scheduleId) throws ServiceFailedException {
//		String url = createScheduleItemsResource(companyId, scheduleId);
//
//		delete(url);
//		return new RpcResultInfo();
//	}
	
//	private ArrayList<ScheduleInfo> parseGetSchedules(Document doc) {
//		ArrayList<ScheduleInfo> schedules = new ArrayList<ScheduleInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("schedule");
//
//			for (int i = 0; i < nodeList.getLength(); i++) {
//
//				Node fstNode = nodeList.item(i);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					ScheduleInfo c = new ScheduleInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					c.setId(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.ID));
//					c.setName(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.NAME));
//					schedules.add(c);
//				}
//			}
//			return schedules;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

//	private String createSchedulesBookmarksResource(SchedulesInfo schedulesInfo) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/schedules?bookmark=pageBookmark
//			
//		String resourceUrl = "/company/" + schedulesInfo.getCompanyId() + "/schedules/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(schedulesInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + schedulesInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name&"; //sortBy is Required
//			
//		if (schedulesInfo.getSearchFor() != null){
//			resourceUrl = resourceUrl + "searchFor=" + schedulesInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(schedulesInfo.getPageSize());
//
//		return resourceUrl;
//	}

//	private String createSchedulesResource(String companyId, String bookmark) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/schedules?bookmark=pageBookmark
//
//		return "/company/" + companyId + "/schedules?bookmark=" + bookmark;
//	}
	
	private String createScheduleResource(String companyId, String scheduleId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/schedule/{scheduleId}
			
		return "/company/" + companyId + "/schedule/" + scheduleId;
	}
	
	private String createCheckDistributionResource(String companyId, String scheduleId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/schedule/{scheduleId}/distributioncheck
			
		return "/company/" + companyId + "/schedule/" + scheduleId + "/distributioncheck";
	}
	
//	private String createPlayListItemResource(String companyId, String scheduleId, String itemId) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/schedule/{scheduleId}/item/{itemIndex}
//			
//		return "/company/" + companyId + "/schedule/" + scheduleId + "/item/" + itemId;
//	}

//	private String createScheduleItemsResource(String companyId, String scheduleId) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/schedule/{scheduleId}/items
//			
//		return "/company/" + companyId + "/schedule/" + scheduleId + "/items";
//	}

//	private void fixScheduleItemsId(ArrayList<PlaylistItemInfo> playListItems) {
//		if (playListItems != null)
//			for (int i = 0; i < playListItems.size(); i++) 
//				playListItems.get(i).setId(Integer.toString(i));
//	}

	private ScheduleInfo DocToSchedule(Document doc) {
		ScheduleInfo schedule = new ScheduleInfo();

		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) rootNode;
				schedule.setId(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.ID));
				schedule.setName(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.NAME));
				//parse default settings
//				schedule.setTransition(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.TRANSITION));
//				schedule.setScale(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.SCALE));
//				schedule.setPosition(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.POSITION));
				
				schedule.setChangedBy(ServerUtils.getNodeValue(fstElmnt, CommonAttribute.CHANGED_BY));
				schedule.setChangeDate(ServerUtils.strToDate(ServerUtils.getNodeValue(fstElmnt, CommonAttribute.CHANGE_DATE), null));

				TimeLineInfo timeline = TimeLineUtils.parseTimeLine(fstElmnt);
				schedule.setTimeline(timeline);
				
				schedule.setDistributionToAll(ServerUtils.StrToBoolean(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.DISTRIBUTE_TO_ALL), false));
				schedule.setDistribution(parseDistribution(fstElmnt));
				
				schedule.setContent(ServerUtils.getNodeValue(fstElmnt, ScheduleAttribute.CONTENT));
				//load Play List Items
				//ArrayList<PlayListItemInfo> playListItems = new ArrayList<PlayListItemInfo>();
//				schedule.setPlayListItems(parsePlayListItems(fstElmnt));

			}	
			return schedule;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private ArrayList<String> parseDistribution(Node parentNode) {
		
		ArrayList<String> distribution = new ArrayList<String>();
		
		Node distributionNode = ServerUtils.findFirstNode(parentNode, ScheduleAttribute.DISTRIBUTION);
		if (distributionNode != null) {
			NodeList nodeList = distributionNode.getChildNodes();
			if (nodeList != null) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if ((node.getNodeType() == Node.ELEMENT_NODE) && (ScheduleAttribute.DISPLAY_ID.equals(node.getNodeName()))) {
						distribution.add(node.getTextContent());
					}
				}
			}
		}
		
		return distribution;
	}
	
//	private ArrayList<PlaylistItemInfo> parsePlayListItems(Node parentNode) {
//		
//		ArrayList<PlaylistItemInfo> playListItems = new ArrayList<PlaylistItemInfo>();
//		
//		Node distributionNode = ServerUtils.findFirstNode(parentNode, "items");
//		if (distributionNode != null) {
//			NodeList nodeList = distributionNode.getChildNodes();
//			if (nodeList != null) {
//				for (int i = 0; i < nodeList.getLength(); i++) {
//					Node node = nodeList.item(i);
//					if ((node.getNodeType() == Node.ELEMENT_NODE) && ("item".equals(node.getNodeName()))) {
//						PlaylistItemInfo pli = new PlaylistItemInfo();
//						Element element = (Element)node;
//						
//						pli.setId(Integer.toString(i));
//						pli.setName(ServerUtils.getNodeValue(element, ScheduleItemAttribute.NAME));
//						pli.setDuration(ServerUtils.getNodeValue(element, ScheduleItemAttribute.DURATION, "10"));
//						pli.setObjectData(ServerUtils.getNodeValue(element, ScheduleItemAttribute.URL));
//						pli.setType(ServerUtils.getNodeValue(element, ScheduleItemAttribute.TYPE));
//						pli.setObjectRef(ServerUtils.getNodeValue(element, ScheduleItemAttribute.OBJECT_REFERENCE));
//						
//						TimeLineInfo timeline = TimeLineUtils.parseTimeLine(element);
//						pli.setTimeline(timeline);
//						
//						pli.setDistributionToAll(ServerUtils.StrToBoolean(ServerUtils.getNodeValue(element, ScheduleItemAttribute.DISTRIBUTE_TO_ALL), false));
//						pli.setDistribution(parseDistribution(element));
//
//						//parse default settings
////						pli.setTransition(ServerUtils.getNodeValue(element, ScheduleItemAttribute.TRANSITION));
////						pli.setScale(ServerUtils.getNodeValue(element, ScheduleItemAttribute.SCALE));
////						pli.setPosition(ServerUtils.getNodeValue(element, ScheduleItemAttribute.POSITION));
//						
//						pli.setChangedBy(ServerUtils.getNodeValue(element, CommonAttribute.CHANGED_BY));
//						pli.setChangeDate(ServerUtils.strToDate(ServerUtils.getNodeValue(element, CommonAttribute.CHANGE_DATE), null));
//						
//						playListItems.add(pli);
//					}
//				}
//			}
//		}
//		
//		return playListItems;
//	}

	private DistributionCheckInfo docToDistributionCheck(Document doc) {
		DistributionCheckInfo distributionCheck = new DistributionCheckInfo();

		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) rootNode;
				Node scheduleAllNode = ServerUtils.findFirstNode(fstElmnt, "scheduleAll");
				if (scheduleAllNode != null) {
					NodeList nodeList = scheduleAllNode.getChildNodes();
					if (nodeList != null && nodeList.getLength() > 0) {
						ArrayList<DistributionCheckScheduleInfo> scheduleAll = new ArrayList<DistributionCheckScheduleInfo>();
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							if ((node.getNodeType() == Node.ELEMENT_NODE) && ("schedule".equals(node.getNodeName()))) {
								DistributionCheckScheduleInfo scheduleInfo = distributionCheck.new DistributionCheckScheduleInfo();
								
								Element element = (Element)node;
								scheduleInfo.setId(ServerUtils.getNodeValue(element, ScheduleAttribute.ID));
								scheduleInfo.setName(ServerUtils.getNodeValue(element, ScheduleAttribute.NAME));
								
								scheduleAll.add(scheduleInfo);
							}
						}
						
						distributionCheck.setScheduleAll(scheduleAll);
					}
				}
				Node scheduleAnyNode = ServerUtils.findFirstNode(fstElmnt, "scheduleAny");
				if (scheduleAnyNode != null) {
					NodeList nodeList = scheduleAnyNode.getChildNodes();
					if (nodeList != null && nodeList.getLength() > 0) {
						ArrayList<DistributionCheckScheduleInfo> scheduleAny = new ArrayList<DistributionCheckScheduleInfo>();
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							if ((node.getNodeType() == Node.ELEMENT_NODE) && ("schedule".equals(node.getNodeName()))) {
								DistributionCheckScheduleInfo scheduleInfo = distributionCheck.new DistributionCheckScheduleInfo();
								
								Element element = (Element)node;
								scheduleInfo.setId(ServerUtils.getNodeValue(element, ScheduleAttribute.ID));
								scheduleInfo.setName(ServerUtils.getNodeValue(element, ScheduleAttribute.NAME));
								
								scheduleAny.add(scheduleInfo);
							}
						}
						
						distributionCheck.setScheduleAny(scheduleAny);
					}
				}
				Node displayNode = ServerUtils.findFirstNode(fstElmnt, "displays");
				if (displayNode != null) {
					NodeList nodeList = displayNode.getChildNodes();
					if (nodeList != null && nodeList.getLength() > 0) {
						ArrayList<DistributionCheckDisplayInfo> displays = new ArrayList<DistributionCheckDisplayInfo>();
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							if ((node.getNodeType() == Node.ELEMENT_NODE) && ("display".equals(node.getNodeName()))) {
								DistributionCheckDisplayInfo displayInfo = distributionCheck.new DistributionCheckDisplayInfo();
								
								Element element = (Element)node;
								displayInfo.setId(ServerUtils.getNodeValue(element, ScheduleAttribute.ID));
								displayInfo.setName(ServerUtils.getNodeValue(element, ScheduleAttribute.NAME));
								
								Node schedulesNode = ServerUtils.findFirstNode(element, "schedules");
								if (schedulesNode != null) {
									NodeList scheduleNodeList = schedulesNode.getChildNodes();
									if (scheduleNodeList != null && scheduleNodeList.getLength() > 0) {
										ArrayList<DistributionCheckScheduleInfo> schedules = new ArrayList<DistributionCheckScheduleInfo>();
										for (int j = 0; j < scheduleNodeList.getLength(); j++) {
											Node scheduleNode = scheduleNodeList.item(j);
											if ((scheduleNode.getNodeType() == Node.ELEMENT_NODE) && ("schedule".equals(scheduleNode.getNodeName()))) {
												DistributionCheckScheduleInfo scheduleInfo = distributionCheck.new DistributionCheckScheduleInfo();
												
												Element scheduleElement = (Element)scheduleNode;
												scheduleInfo.setId(ServerUtils.getNodeValue(scheduleElement, ScheduleAttribute.ID));
												scheduleInfo.setName(ServerUtils.getNodeValue(scheduleElement, ScheduleAttribute.NAME));
												
												schedules.add(scheduleInfo);
											}
										}
										
										displayInfo.setSchedule(schedules);
									}
								}
								
								displays.add(displayInfo);
							}
						}
						
						distributionCheck.setDisplay(displays);
					}
				}

			}	
			return distributionCheck;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
