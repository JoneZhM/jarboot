import {memo, useEffect, useState} from "react";
import { useIntl } from 'umi';
import {CommonTable} from "@/components";
import {JarBootConst} from "@/common/JarBootConst";
import {formatMsg} from "@/common/IntlFormat";
import {DeleteOutlined, SyncOutlined, PlusSquareOutlined, ExclamationCircleOutlined} from "@ant-design/icons";
import CommonNotice from "@/common/CommonNotice";
import ErrorUtil from "@/common/ErrorUtil";
import RoleService from "@/services/RoleService";
import {Form, Input, Modal} from "antd";

const toolButtonStyle = {color: '#1890ff', fontSize: '18px'};
const toolButtonRedStyle = {color: 'red', fontSize: '18px'};

const RoleMgr = memo(() => {
    const intl = useIntl();
    const [form] = Form.useForm();
    let [loading, setLoading] = useState(false);
    let [visible, setVisible] = useState(false);
    let [selected, setSelected] = useState({
        selectedRowKeys: new Array<any>(), selectedRows: new Array<any>()
    });
    let [data, setData] = useState(new Array<any>());

    useEffect(() => query(), []);

    const query = () => {
        setLoading(true);
        RoleService.getRoles(0, 10000000).then(resp => {
            if (resp.resultCode !== 0) {
                CommonNotice.error(ErrorUtil.formatErrResp(resp));
                return;
            }
            setData(resp.result);
            setLoading(false);
        }).catch(error => CommonNotice.error(ErrorUtil.formatErrResp(error)));
    };

    const _getRowSelection = () => {
        return {
            type: 'radio',
            onChange: (selectedRowKeys: any, selectedRows: any) => {
                setSelected({selectedRowKeys, selectedRows});
            },
            selectedRowKeys: selected.selectedRowKeys,
        };
    };

    const _onRowClick = (record: any) => {
        return {
            onClick: () => setSelected({selectedRowKeys: [record.id], selectedRows: [record]}),
        };
    };

    let tableOption: any = {
        columns: [
            {
                title: formatMsg('ROLE'),
                dataIndex: 'roleInfo',
                key: 'roleInfo',
                ellipsis: true,
            },
            {
                title: formatMsg('NAME'),
                dataIndex: 'username',
                key: 'username',
                ellipsis: true,
            },
        ],
        loading: loading,
        dataSource: data,
        pagination: false,
        rowKey: 'id',
        size: 'small',
        rowSelection: _getRowSelection(),
        onRow: _onRowClick,
        showHeader: true,
        scroll: JarBootConst.PANEL_HEIGHT,
    };

    const onBindClick = () => {
        setVisible(true);
    };

    const onDeleteClick = () => {
        if (selected?.selectedRows && selected.selectedRows.length !== 1) {
            CommonNotice.info(intl.formatMessage({id: 'SELECT_ONE_OP'}));
            return;
        }
        const roleInfo = selected.selectedRows[0];
        Modal.confirm({
            title: intl.formatMessage({id: 'DELETE'}),
            icon: <ExclamationCircleOutlined />,
            content: intl.formatMessage({id: 'DELETE_ROLE'}),
            onOk() {
                RoleService.deleteRole(roleInfo.roleInfo, roleInfo.username).then(resp => {
                    if (0 === resp.resultCode) {
                        CommonNotice.info(intl.formatMessage({id: 'SUCCESS'}));
                        query();
                    } else {
                        CommonNotice.error(ErrorUtil.formatErrResp(resp));
                    }
                }).catch(error => CommonNotice.error(ErrorUtil.formatErrResp(error)));
            }
        });
    };

    const onModalClose = () => {
        setVisible(false);
    };

    const onOk = () => {
        form.submit();
    };

    const onSubmit = (data: any) => {
        //提交
        RoleService.addRole(data.roleInfo, data.username).then(resp => {
            if (0 === resp.resultCode) {
                onModalClose();
                CommonNotice.info(intl.formatMessage({id: 'SUCCESS'}));
                query();
            } else {
                CommonNotice.error(ErrorUtil.formatErrResp(resp));
            }
        }).catch(error => CommonNotice.error(ErrorUtil.formatErrResp(error)));
    };

    const _getTbBtnProps = () => {
        return [
            {
                name: intl.formatMessage({id: 'BIND_ROLE'}),
                key: 'banding ',
                icon: <PlusSquareOutlined style={toolButtonStyle}/>,
                onClick: onBindClick,
            },
            {
                name: intl.formatMessage({id: 'DELETE'}),
                key: 'delete',
                icon: <DeleteOutlined style={toolButtonRedStyle}/>,
                onClick: onDeleteClick,
            },
            {
                name: intl.formatMessage({id: 'REFRESH_BTN'}),
                key: 'refresh',
                icon: <SyncOutlined style={toolButtonStyle}/>,
                onClick: query,
            },
        ]
    };

    tableOption.scroll = { y: JarBootConst.PANEL_HEIGHT};
    const style = {height: '38px', fontSize: '16px', width: '100%'};
    return <>
        <CommonTable option={tableOption}
                     toolbar={_getTbBtnProps()} showToolbarName={true}
                     height={JarBootConst.PANEL_HEIGHT}/>
        {visible && <Modal title={intl.formatMessage({id: 'BIND_ROLE'})}
               visible={true}
               destroyOnClose={true}
               onOk={onOk}
               onCancel={onModalClose}>
            <Form form={form}
                  name="roleInfo-binding-form"
                  initialValues={{username: '', roleInfo: ''}}
                  onFinish={onSubmit}>
                <Form.Item name="roleInfo"
                           rules={[{ required: true, message: intl.formatMessage({id: 'INPUT_ROLE'}) }]}>
                    <Input autoComplete="off"
                           autoCorrect="off"
                           autoCapitalize="off"
                           spellCheck="false"
                           placeholder={intl.formatMessage({id: 'ROLE'})} style={style}/>
                </Form.Item>
                <Form.Item name="username"
                           rules={[{ required: true, message: intl.formatMessage({id: 'INPUT_USERNAME'})}]}>
                    <Input autoComplete="off"
                           autoCorrect="off"
                           autoCapitalize="off"
                           spellCheck="false"
                           placeholder={intl.formatMessage({id: 'USER_NAME'})} style={style}/>
                </Form.Item>
            </Form>
        </Modal>}
    </>;
});

export default RoleMgr;
